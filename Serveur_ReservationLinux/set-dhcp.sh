#!/usr/bin/env bash
# set-dhcp.sh
# Active le mode DHCP sur une interface et affiche la nouvelle IP
# Usage : sudo ./set-dhcp.sh [interface]
# Exemple : sudo ./set-dhcp.sh ens160

set -euo pipefail

# Interface par défaut
IFACE="${1:-ens160}"

# Vérifie que le script est lancé en root
if [[ $EUID -ne 0 ]]; then
  echo "❌ Ce script doit être exécuté avec sudo ou en root."
  exit 1
fi

echo "=== 🔁 Passage de $IFACE en mode DHCP ==="

# Vérifie si nmcli est dispo
if command -v nmcli >/dev/null 2>&1; then
  # Trouver le nom de la connexion associée
  CON_NAME=$(nmcli -t -f NAME,DEVICE connection show | awk -F: -v dev="$IFACE" '$2==dev {print $1; exit}')

  if [[ -z "$CON_NAME" ]]; then
    echo "⚠️ Aucune connexion trouvée pour $IFACE, tentative de création..."
    CON_NAME="dhcp-$IFACE"
    nmcli connection add type ethernet ifname "$IFACE" con-name "$CON_NAME" >/dev/null
  fi

  echo "→ Connexion détectée : $CON_NAME"
  echo "→ Application du mode DHCP..."
  nmcli connection modify "$CON_NAME" ipv4.method auto ipv4.addresses "" ipv4.gateway "" ipv4.dns "" >/dev/null

  echo "→ Redémarrage de la connexion..."
  nmcli connection down "$CON_NAME" >/dev/null 2>&1 || true
  nmcli connection up "$CON_NAME" >/dev/null 2>&1 || true

else
  # Fallback : modification du fichier réseau
  CFG="/etc/sysconfig/network-scripts/ifcfg-${IFACE}"
  echo "⚙️ nmcli introuvable — modification directe du fichier $CFG"
  if [[ -f "$CFG" ]]; then
    cp "$CFG" "${CFG}.bak.$(date +%Y%m%d%H%M%S)"
  fi

  cat > "$CFG" <<EOF
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=dhcp
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
NAME=${IFACE}
DEVICE=${IFACE}
ONBOOT=yes
EOF

  echo "→ Fichier mis à jour pour le DHCP."
  systemctl restart NetworkManager || systemctl restart network || true
fi

# Attendre un peu que l'IP soit attribuée
sleep 3

echo
echo "=== 🌐 Nouvelle adresse IP ==="
ip -4 addr show dev "$IFACE" | awk '/inet /{print "→ " $2 " (" $NF ")"}' || echo "❌ Impossible de lire l'adresse IP."
echo
echo "✅ DHCP activé avec succès sur $IFACE"