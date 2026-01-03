<script setup lang="ts">
import { ref, onMounted } from 'vue'

import LoginForm from '@/components/LoginForm.vue'
import ConsultationTable from '@/components/ConsultationTable.vue'
import AvailableConsultations from '@/components/AvailableConsultations.vue'

import type { Patient } from '@/model/entity/Patient'
import type { Consultation } from '@/model/entity/Consultation'
import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'
import type { Doctor } from '@/model/entity/Doctor'
import type { Specialty } from '@/model/entity/Speciality'

import { PatientDAO_API } from '@/model/dao/implementation/PatientDAO_API'
import { ConsultationDAO_API } from '@/model/dao/implementation/ConsultationDAO_API'
import { DoctorDAO_API } from '@/model/dao/implementation/DoctorDAO_API'
import { SpecialtyDAO_API } from '@/model/dao/implementation/SpecialtyDAO_API'

// =======================
// STATE GLOBAL
// =======================
const connectedPatient = ref<Patient | null>(null)
const consultations = ref<Consultation[]>([])
const availableConsultations = ref<Consultation[]>([])

const doctors = ref<Doctor[]>([])
const specialties = ref<Specialty[]>([])

const selectedDoctorId = ref<number | null>(null)
const selectedSpecialtyId = ref<number | null>(null)
const selectedConsultationId = ref<number | null>(null)

const isConnected = ref(false)
const showAvailableConsultations = ref(false)

// =======================
// DAO
// =======================
const daoPatient = new PatientDAO_API()
const daoConsultation = new ConsultationDAO_API()
const doctorDAO = new DoctorDAO_API()
const specialtyDAO = new SpecialtyDAO_API()

// =======================
// INIT GLOBAL
// =======================
onMounted(async () => {
  doctors.value = await doctorDAO.load()
  specialties.value = await specialtyDAO.load()
})

// =======================
// LOGIN
// =======================
async function handleLogin(payload: {
  lastname: string
  firstname: string
  patientId: string | null
  isNewPatient: boolean
}) {
  const patient: Patient = {
    id: payload.patientId ? Number(payload.patientId) : undefined,
    firstname: payload.firstname,
    lastname: payload.lastname,
    birthDate: '01/01/2000', // requis backend
  }

  const id = await daoPatient.save(patient, payload.isNewPatient)
  patient.id = id

  connectedPatient.value = patient
  isConnected.value = true

  await loadConsultations(patient)
}

// =======================
// MES CONSULTATIONS
// =======================
async function loadConsultations(patient: Patient) {
  const vm: ConsultationVM = {
    patientId: patient.id!,
  }

  consultations.value = await daoConsultation.load(vm)
}

// =======================
// CONSULTATIONS DISPONIBLES
// =======================
async function loadAvailableConsultations() {
  availableConsultations.value = await daoConsultation.load()
  availableConsultations.value = availableConsultations.value.filter(
    c => c.patientId == null
  )
}

// =======================
// FILTRES DISPONIBLES
// =======================
async function applyAvailableFilters() {
  const params: any = {}

  if (selectedDoctorId.value != null) {
    const doctor = doctors.value.find(d => d.id === selectedDoctorId.value)
    if (doctor) params.doctor = doctor.lastname
  }

  if (selectedSpecialtyId.value != null) {
    const specialty = specialties.value.find(s => s.id === selectedSpecialtyId.value)
    if (specialty) params.specialty = specialty.name
  }

  availableConsultations.value = await daoConsultation.load(params)
  availableConsultations.value = availableConsultations.value.filter(
    c => c.patientId == null
  )
}

// =======================
// RESERVER
// =======================
async function reserveConsultation(consultationId: number, reason: string) {
  if (!connectedPatient.value) return

  const consultation = availableConsultations.value.find(
    c => c.id === consultationId
  )
  if (!consultation) return

  consultation.patientId = connectedPatient.value.id!
  consultation.reason = reason

  await daoConsultation.save(consultation)

  showAvailableConsultations.value = false
  await loadConsultations(connectedPatient.value)
}

// =======================
// DELETE CONSULTATION
// =======================
async function deleteConsultation(id: string) {
  await daoConsultation.delete(id)
  await loadConsultations(connectedPatient.value!)
}

// =======================
// LOGOUT
// =======================
function logout() {
  isConnected.value = false
  showAvailableConsultations.value = false

  connectedPatient.value = null
  consultations.value = []
  availableConsultations.value = []

  selectedDoctorId.value = null
  selectedSpecialtyId.value = null
  selectedConsultationId.value = null
}
</script>

<template>
  <div class="main-container">
    <!-- ===================== -->
    <!-- AVANT CONNEXION -->
    <!-- ===================== -->
    <LoginForm
      v-if="!isConnected"
      @login="handleLogin"
    />

    <!-- ===================== -->
    <!-- APRÈS CONNEXION -->
    <!-- ===================== -->
    <div v-else>
      <p class="mb-3">
        <strong>Patient connecté :</strong>
        {{ connectedPatient?.firstname }}
        {{ connectedPatient?.lastname }}
        (ID : {{ connectedPatient?.id }})
      </p>

      <!-- MES RDV -->
      <ConsultationTable
        :consultations="consultations"
        @logout="logout"
        @delete-consultation="deleteConsultation"
        @new-consultation="async () => {
          showAvailableConsultations = true
          await loadAvailableConsultations()
        }"
      />

      <!-- RDV DISPONIBLES -->
      <AvailableConsultations
        v-if="showAvailableConsultations"
        :consultations="availableConsultations"
        :doctors="doctors"
        :specialties="specialties"
        v-model:selectedDoctorId="selectedDoctorId"
        v-model:selectedSpecialtyId="selectedSpecialtyId"
        v-model:selectedConsultationId="selectedConsultationId"
        @search="applyAvailableFilters"
        @reserve="reserveConsultation"
        @cancel="showAvailableConsultations = false"
      />
    </div>
  </div>
</template>

<style scoped>
.main-container {
  padding: 2rem;
}
</style>
