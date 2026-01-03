<script setup lang="ts">
import { ref, onMounted } from 'vue'

import type { Consultation } from '@/model/entity/Consultation'
import type { Doctor } from '@/model/entity/Doctor'
import type { Specialty } from '@/model/entity/Speciality'

import { ConsultationDAO_API } from '@/model/dao/implementation/ConsultationDAO_API'
import { DoctorDAO_API } from '@/model/dao/implementation/DoctorDAO_API'
import { SpecialtyDAO_API } from '@/model/dao/implementation/SpecialtyDAO_API'

// =======================
// PROPS / EMITS
// =======================
const props = defineProps<{
  patientId: number
}>()

const emit = defineEmits<{
  (e: 'reserved'): void
  (e: 'cancel'): void
}>()

// =======================
// STATE
// =======================
const consultations = ref<Consultation[]>([])
const doctors = ref<Doctor[]>([])
const specialties = ref<Specialty[]>([])

const selectedConsultationId = ref<number | null>(null)
const selectedDoctorId = ref<number | null>(null)
const selectedSpecialtyId = ref<number | null>(null)

// =======================
// DAO
// =======================
const consultationDAO = new ConsultationDAO_API()
const doctorDAO = new DoctorDAO_API()
const specialtyDAO = new SpecialtyDAO_API()

// =======================
// INIT
// =======================
onMounted(async () => {
  doctors.value = await doctorDAO.load()
  specialties.value = await specialtyDAO.load()
  await loadConsultations()
})

// =======================
// LOAD CONSULTATIONS
// =======================
async function loadConsultations() {
  consultations.value = await consultationDAO.load()

  // garder uniquement celles qui ne sont pas réservées
  consultations.value = consultations.value.filter(c => c.patientId == null)
}

// =======================
// FILTER (APPEL MANUEL)
// =======================
async function applyFilters() {
  consultations.value = await consultationDAO.load({
    doctorId: selectedDoctorId.value ?? undefined,
  })

  consultations.value = consultations.value.filter(c => c.patientId == null)
}

// =======================
// RESERVE
// =======================
async function reserve() {
  if (selectedConsultationId.value == null) {
    alert('Veuillez sélectionner une consultation')
    return
  }

  const reason = prompt('Raison de la consultation ?')
  if (reason == null || reason === '') return

  const consultation = consultations.value.find(
    c => c.id === selectedConsultationId.value
  )

  if (!consultation) return

  consultation.patientId = props.patientId
  consultation.reason = reason

  await consultationDAO.save(consultation)

  emit('reserved')
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

      <button class="btn btn-primary" @click="applyFilters">
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
        <td>{{ c.doctorName }}</td>
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
}
</style>
