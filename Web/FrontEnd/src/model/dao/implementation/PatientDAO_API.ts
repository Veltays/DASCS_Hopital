import type { PatientAccessLayer } from '../PatientAccessLayer.ts'
import type { Patient } from '../../entity/Patient.ts'
import type { PatientVM } from '../../viewmodel/PatientVM.ts'

// Exception personnalisée pour l'absence de Patient
export class PatientsNotFoundError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class PatientDAO_API implements PatientAccessLayer {
  private selectedPatients: Array<Patient>
  private API_ENDPOINT: string = 'http://localhost:8081/api/patients'

  constructor() {
    this.selectedPatients = []
  }

  public getList(): Array<Patient> {
    return this.selectedPatients
  }

  // ===== LOAD (GET + query params EXACTEMENT COMME SPECIALTY) =====
  public async load(vm?: PatientVM): Promise<Array<Patient>> {
    this.selectedPatients = []

    if (vm) {
      const params = new URLSearchParams()

      if (vm.id) {
        params.append('id', String(vm.id))
      }
      if (vm.firstname) {
        params.append('firstName', vm.firstname)
      }
      if (vm.lastname) {
        params.append('lastName', vm.lastname)
      }

      const res = await fetch(`${this.API_ENDPOINT}?${params.toString()}`)

      if (res.ok) {
        this.selectedPatients = await res.json()
      } else {
        throw new PatientsNotFoundError('Erreur de load')
      }
    } else {
      const res = await fetch(`${this.API_ENDPOINT}`)
      if (res.ok) {
        this.selectedPatients = await res.json()
      } else {
        throw new PatientsNotFoundError('Erreur de load')
      }
    }

    return this.selectedPatients
  }

  // ===== SAVE (POST si nouveau / PUT si existant EXACTEMENT COMME SPECIALTY) =====
  public async save(patientToSave: Patient): Promise<void> {
    if (patientToSave.id == null) {
      // ajout
      const newPatient: Patient = {
        id: -1,
        firstname: patientToSave.firstname,
        lastname: patientToSave.lastname,
        birthDate: patientToSave.birthDate,
      }

      const res = await fetch(this.API_ENDPOINT, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newPatient),
      })

      const patientAdded = await res.json()
      console.log(patientAdded)
    } else {
      // modification
      await fetch(`${this.API_ENDPOINT}/${patientToSave.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(patientToSave),
      })
    }
  }

  // ===== DELETE (DELETE EXACTEMENT COMME SPECIALTY) =====
  public async delete(item: null | string | Patient): Promise<void> {
    if (item == null) {
      throw new PatientsNotFoundError('Erreur de delete: paramètre nul')
    }

    let id: string

    if (typeof item === 'string') {
      id = item
    } else if (item.id) {
      id = String(item.id)
    } else {
      throw new PatientsNotFoundError('Erreur de delete: Patient sans id')
    }

    const res = await fetch(`${this.API_ENDPOINT}/${id}`, {
      method: 'DELETE',
    })

    if (!res.ok) {
      throw new PatientsNotFoundError(`Patient avec id ${id} pas trouvé`)
    }

    console.log(`Patient ${id} supprimé avec succès`)
  }
}
