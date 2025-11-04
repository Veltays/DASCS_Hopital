#!/usr/bin/env bash
# set-static-ip.sh
# Usage: sudo ./set-static-ip.sh [IP] [PREFIX] [GATEWAY] [DNS]
# Exemple: sudo ./set-static-ip.sh 192.168.0.43 24 192.168.0.1 8.8.8.8

set -euo pipefail

IFACE="ens160"
DEFAULT_IP="192.168.0.43"
DEFAULT_PREFIX="24"
DEFAULT_GW="192.168.0.1"
DEFAULT_DNS="8.8.8.8"

IP="${1:-$DEFAULT_IP}"
PREFIX="${2:-$DEFAULT_PREFIX}"
GATEWAY="${3:-$DEFAULT_GW}"
DNS="${4:-$DEFAULT_DNS}"

# Check root
if [[ $EUID -ne 0 ]]; then
  echo "Ce script doit être exécuté en root (sudo)." >&2
  exit 1
fi

echo "=== Forcer IP statique sur $IFACE ==="
echo "IP: $IP/$PREFIX  GW: $GATEWAY  DNS: $DNS"

# Verify interface exists
if ! ip link show "$IFACE" >/dev/null 2>&1; then
  echo "Erreur: interface $IFACE introuvable." >&2
  ip link show
  exit 2
fi

# Try nmcli method first
if command -v nmcli >/dev/null 2>&1; then
  echo "- Utilisation de nmcli (méthode recommandée)..."

  # Get connection name(s) for this device
  CON_NAME=$(nmcli -t -f NAME,DEVICE connection show --active | awk -F: -v dev="$IFACE" '$2==dev {print $1; exit}')
  if [[ -z "$CON_NAME" ]]; then
    # try to find any connection for device
    CON_NAME=$(nmcli -t -f NAME,DEVICE connection show | awk -F: -v dev="$IFACE" '$2==dev {print $1; exit}')
  fi

  if [[ -z "$CON_NAME" ]]; then
    # fallback: create a new connection
    CON_NAME="static-$IFACE"
    echo "  > Aucune connection NM existante trouvée pour $IFACE. Création d'une connexion $CON_NAME..."
    nmcli connection add type ethernet ifname "$IFACE" con-name "$CON_NAME" >/dev/null
  else
    echo "  > Connexion NetworkManager trouvée: '$CON_NAME'"
  fi

  # Backup existing connection (export)
  BACKUP_DIR="/root/nmcli-backups"
  mkdir -p "$BACKUP_DIR"
  nmcli connection export "$CON_NAME" "$BACKUP_DIR/${CON_NAME}.nmconnection" >/dev/null 2>&1 || true
  echo "  > Sauvegarde de la connexion : $BACKUP_DIR/${CON_NAME}.nmconnection"

  # Apply static IP
  echo "  > Configuration statique via nmcli..."
  nmcli connection modify "$CON_NAME" ipv4.method manual ipv4.addresses "${IP}/${PREFIX}" ipv4.gateway "$GATEWAY" ipv4.dns "$DNS" ipv4.never-default no connection.autoconnect yes >/dev/null

  echo "  > Appliquer et redémarrer la connexion..."
  nmcli connection down "$CON_NAME" >/dev/null 2>&1 || true
  nmcli connection up "$CON_NAME" >/dev/null 2>&1

  echo "- OK (nmcli)"
else
  # Fallback: edit /etc/sysconfig/network-scripts/ifcfg-IFACE
  CFG="/etc/sysconfig/network-scripts/ifcfg-${IFACE}"
  echo "- nmcli introuvable, édition directe de $CFG ..."

  # Backup
  if [[ -f "$CFG" ]]; then
    cp -v "$CFG" "${CFG}.bak.$(date +%Y%m%d%H%M%S)"
  else
    echo "# Fichier créé par set-static-ip.sh" > "$CFG"
  fi

  cat > "$CFG" <<EOF
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=none
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
NAME=${IFACE}
DEVICE=${IFACE}
ONBOOT=yes
IPADDR=${IP}
PREFIX=${PREFIX}
GATEWAY=${GATEWAY}
DNS1=${DNS}
EOF

  echo "  > Fichier $CFG mis à jour (sauvegarde si existante)."

  # Restart NetworkManager
  if systemctl is-active --quiet NetworkManager; then
    echo "  > Reload NetworkManager..."
    nmcli connection reload >/dev/null 2>&1 || true
    systemctl restart NetworkManager
  else
    echo "  > NetworkManager non actif, tentative de redémarrage du service network..."
    systemctl restart network || true
  fi
fi

# Small wait then show resulting IP
sleep 2
echo
echo "=== Résultat ==="
ip -4 addr show dev "$IFACE" | awk '/inet /{print $2 " (" $NF ")"}' || echo "Impossible d'afficher l'IP."

