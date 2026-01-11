<script setup lang="ts">
import { ref } from 'vue'

// State
const lastname = ref('')
const firstname = ref('')
const patientId = ref('')
const isNewPatient = ref(false)

// Emits (OBLIGATOIRE)
const emit = defineEmits<{
  (e: 'login', payload: {
    lastname: string
    firstname: string
    patientId: string | null
    isNewPatient: boolean
  }): void
}>()

// Method
function login() {
  emit('login', {
    lastname: lastname.value,
    firstname: firstname.value,
    patientId: isNewPatient.value ? null : patientId.value,
    isNewPatient: isNewPatient.value,
  })

}
</script>

<template>
  <fieldset>
    <legend>Entrée en session patient</legend>

    <div>
      <label>Nom :</label>
      <input type="text" v-model="lastname" />
    </div>

    <div>
      <label>Prénom :</label>
      <input type="text" v-model="firstname" />
    </div>

    <div v-if="!isNewPatient">
      <label>Numéro de patient :</label>
      <input type="number" v-model="patientId" />
    </div>

    <div>
      <label>
        <input type="checkbox" v-model="isNewPatient" />
        Nouveau patient
      </label>
    </div>


    <button class="btn btn-primary" @click="login">
      Se connecter
    </button>

  </fieldset>

</template>


<style scoped>
fieldset {
  padding: 1rem;
  border: 1px solid #ccc;
}

div {
  margin-bottom: 0.5rem;
}

label {
  display: inline-block;
  width: 150px;
}

btn{
  margin-top: 1rem;
}

.btn-primary{
  background-color: darkslategray;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;

}

</style>
