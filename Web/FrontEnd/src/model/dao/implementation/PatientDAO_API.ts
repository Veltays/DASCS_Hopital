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
  private API_ENDPOINT: string = '/api/patients'

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

  public async save(
    patientToSave: Patient,
    newPatient: boolean
  ): Promise<number> {

    const body: any = {
      lastName: patientToSave.lastname,
      firstName: patientToSave.firstname,
      birthDate: patientToSave.birthDate, // OBLIGATOIRE
      newPatient: newPatient,
    }

    if (!newPatient) {
      body.patientId = patientToSave.id
    }

    const res = await fetch(this.API_ENDPOINT, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })

    if (!res.ok) {
      throw new PatientsNotFoundError(
        'Erreur lors de la création / identification du patient'
      )
    }

    const id = await res.json()
    return Number(id)
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
