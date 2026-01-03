<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

import type { Consultation } from '@/model/entity/Consultation'
import type { Doctor } from '@/model/entity/Doctor'
import type { Specialty } from '@/model/entity/Speciality'

import { DoctorDAO_API } from '@/model/dao/implementation/DoctorDAO_API'
import { SpecialtyDAO_API } from '@/model/dao/implementation/SpecialtyDAO_API'

// =======================
// PROPS
// =======================
const props = defineProps<{
  consultations: Consultation[]
}>()

// =======================
// STATE LOCAL
// =======================
const selectedConsultationId = ref<number  | null>(null)
const doctors = ref<Doctor[]>([])
const specialties = ref<Specialty[]>([])

// =======================
// DAO
// =======================
const doctorDAO = new DoctorDAO_API()
const specialtyDAO = new SpecialtyDAO_API()

// =======================
// INIT
// =======================
onMounted(async () => {
  doctors.value = await doctorDAO.load()
  specialties.value = await specialtyDAO.load()
})

// =======================
// COMPUTED
// =======================
const nbConsultations = computed(() => props.consultations.length)

// =======================
// UTILS (SIMPLES)
// =======================
function getDoctorName(doctorId: number): string {
  const doctor = doctors.value.find(d => d.id === doctorId)
  return doctor
    ? `${doctor.firstname} ${doctor.lastname}`
    : '—'
}

function getSpecialtyName(doctorId: number): string {
  const doctor = doctors.value.find(d => d.id === doctorId)
  if (!doctor) return '—'

  const specialty = specialties.value.find(
    s => s.id === doctor.specialtyId
  )

  return specialty ? specialty.name : '—'
}

// =======================
// EMITS
// =======================
const emit = defineEmits<{
  (e: 'logout'): void
  (e: 'delete-consultation', id: string): void
  (e: 'new-consultation'): void
}>()

// =======================
// METHODS
// =======================
function onDelete() {
  if (!selectedConsultationId.value) {
    alert('Veuillez sélectionner une consultation')
    return
  }

  if (confirm('Êtes-vous certain de vouloir annuler ce rendez-vous ?')) {
    emit('delete-consultation', selectedConsultationId.value)
  }
}
</script>

<template>
  <div class="mt-5 composant">
    <h2>Mes rendez-vous</h2>

    <p class="text-muted">
      Nombre de consultations : {{ nbConsultations }}
    </p>

    <table class="table table-bordered table-hover">
      <thead>
      <tr>
        <th>Date</th>
        <th>Heure</th>
        <th>Médecin</th>
        <th>Spécialité</th>
        <th>Raison</th>
        <th>Sélection</th>
      </tr>
      </thead>

      <tbody>
      <tr
        v-for="consultation in consultations"
        :key="consultation.id"
      >
        <td>{{ consultation.date }}</td>
        <td>{{ consultation.hour }}</td>
        <td>{{ getDoctorName(consultation.doctorId) }}</td>
        <td>{{ getSpecialtyName(consultation.doctorId) }}</td>
        <td>{{ consultation.reason }}</td>
        <td class="text-center">
          <input
            type="radio"
            name="selectedConsultation"
            :value="consultation.id"
            v-model="selectedConsultationId"
          />
        </td>
      </tr>
      </tbody>
    </table>

    <!-- BOUTONS -->
    <div class="buttons-container">
      <button class="btn btn-secondary" @click="emit('logout')">
        Logout
      </button>

      <button class="btn btn-danger" @click="onDelete">
        Supprimer
      </button>

      <button class="btn btn-primary" @click="emit('new-consultation')">
        Prendre un autre rendez-vous
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

.buttons-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
