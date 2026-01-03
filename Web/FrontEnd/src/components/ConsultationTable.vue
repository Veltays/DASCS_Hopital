<script setup lang="ts">
import { computed, ref } from 'vue'
import type { Consultation } from '@/model/entity/Consultation'

// Props
const props = defineProps<{
  consultations: Consultation[]
}>()

// État local
const selectedConsultationId = ref<string | null>(null)

// Computed
const nbConsultations = computed(() => props.consultations.length)

// Emits
const emit = defineEmits<{
  (e: 'logout'): void
  (e: 'delete-consultation', id: string): void
  (e: 'new-consultation'): void
}>()

// Methods
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

    <div class="table-responsive">
      <table class="table table-striped table-hover table-bordered">
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
          v-for="consultation in props.consultations"
          :key="consultation.id"
        >
          <td>{{ consultation.date }}</td>
          <td>{{ consultation.hour }}</td>
          <td>{{ consultation.doctorName }}</td>
          <td>{{ consultation.specialtyName }}</td>
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
    </div>

    <!-- Boutons -->
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

<style>
.composant {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 900px;
  max-width: 95vw;
  margin: 0 auto;
}

.buttons-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
