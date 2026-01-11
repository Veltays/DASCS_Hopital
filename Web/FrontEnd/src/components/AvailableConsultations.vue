<script setup lang="ts">
import { ref } from 'vue'
import type { Consultation } from '@/model/entity/Consultation'
import type { Doctor } from '@/model/entity/Doctor'
import type { Specialty } from '@/model/entity/Speciality'

// =======================
// PROPS
// =======================


defineProps<{
  consultations: Consultation[]
  doctors: Doctor[]
  specialties: Specialty[]
}>()


// =======================
// EMITS
// =======================
const emit = defineEmits<{
  (e: 'search', doctorId: number | null, specialtyId: number | null): void
  (e: 'reserve', consultationId: number, reason: string): void
  (e: 'cancel'): void
}>()

// =======================
// STATE LOCAL (UI)
// =======================
const selectedDoctorId = ref<number | null>(null)
const selectedSpecialtyId = ref<number | null>(null)
const selectedConsultationId = ref<number | null>(null)

// =======================
// ACTIONS
// =======================
function search() {
  emit('search', selectedDoctorId.value, selectedSpecialtyId.value)
}

function reserve() {
  if (selectedConsultationId.value == null) {
    alert('Veuillez sélectionner une consultation')
    return
  }

  const reason = prompt('Raison de la consultation ?')
  if (!reason) return

  emit('reserve', selectedConsultationId.value, reason)
}
</script>

<template>
  <div class="composant mt-4">
    <h2>Consultations disponibles</h2>

    <!-- FILTRES -->
    <div class="filters">
      <select v-model="selectedSpecialtyId">
        <option :value="null">Toutes les spécialités</option>
        <option
          v-for="s in specialties"
          :key="s.id"
          :value="s.id"
        >
          {{ s.name }}
        </option>
      </select>

      <select v-model="selectedDoctorId">
        <option :value="null">Tous les médecins</option>
        <option
          v-for="d in doctors"
          :key="d.id"
          :value="d.id"
        >
          {{ d.firstname }} {{ d.lastname }}
        </option>
      </select>

      <button class="btn btn-primary" @click="search">
        Rechercher
      </button>
    </div>

    <!-- TABLE -->
    <table class="table table-bordered mt-3">
      <thead>
      <tr>
        <th>Date</th>
        <th>Heure</th>
        <th>Médecin</th>
        <th>Sélection</th>
      </tr>
      </thead>
      <tbody>
      <tr
        v-for="c in consultations"
        :key="c.id"
      >
        <td>{{ c.date }}</td>
        <td>{{ c.hour }}</td>
        <td>{{ c.doctorId }}</td>
        <td class="text-center">
          <input
            type="radio"
            name="selectedConsultation"
            :value="c.id"
            v-model="selectedConsultationId"
          />
        </td>
      </tr>
      </tbody>
    </table>

    <!-- BOUTONS -->
    <div class="buttons-container">
      <button class="btn btn-success" @click="reserve">
        Réserver
      </button>

      <button class="btn btn-secondary" @click="emit('cancel')">
        Annuler
      </button>
    </div>
  </div>
</template>

<style scoped>
.composant {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.filters {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.buttons-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  border-radius: 40px;
}
table th, table td
{
  border : 2px solid black;
  padding : 8px;
}
h2
{
  text-decoration: underline;
  border: 2px solid black;
  text-align : center;
}
th
{
  font-weight:bolder;
  background: dimgray;
  color:black;
  padding : 12px;
}
</style>
