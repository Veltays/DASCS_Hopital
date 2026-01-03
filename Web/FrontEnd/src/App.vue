<script setup lang="ts">
import { ref } from 'vue'

import LoginForm from '@/components/LoginForm.vue'
import ConsultationTable from '@/components/ConsultationTable.vue'
import AvailableConsultations from '@/components/AvailableConsultations.vue'

import type { Patient } from '@/model/entity/Patient'
import type { Consultation } from '@/model/entity/Consultation'
import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'

import { PatientDAO_API } from '@/model/dao/implementation/PatientDAO_API'
import { ConsultationDAO_API } from '@/model/dao/implementation/ConsultationDAO_API'

// =======================
// STATE GLOBAL
// =======================
const connectedPatient = ref<Patient | null>(null)
const consultations = ref<Consultation[]>([])

const isConnected = ref(false)
const showAvailableConsultations = ref(false)

// =======================
// DAO
// =======================
const daoPatient = new PatientDAO_API()
const daoConsultation = new ConsultationDAO_API()

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
    birthDate: undefined,
  }

  // POST si nouveau / PUT si existant (logique API)
  await daoPatient.save(patient)

  connectedPatient.value = patient
  isConnected.value = true

  await loadConsultations(patient)
}

// =======================
// LOAD CONSULTATIONS PATIENT
// =======================
async function loadConsultations(patient: Patient) {
  const vm: ConsultationVM = {
    patientId: patient.id!,
  }

  consultations.value = await daoConsultation.load(vm)
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

      <!-- ===================== -->
      <!-- 2e composant : MES RDV -->
      <!-- ===================== -->
      <ConsultationTable
        :consultations="consultations"
        @logout="logout"
        @delete-consultation="deleteConsultation"
        @new-consultation="showAvailableConsultations = true"
      />

      <!-- ===================== -->
      <!-- 3e composant : DISPONIBLES -->
      <!-- ===================== -->
      <AvailableConsultations
        v-if="showAvailableConsultations"
        :patient-id="connectedPatient!.id!"
        @reserved="async () => {
          showAvailableConsultations = false
          await loadConsultations(connectedPatient!)
        }"
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
