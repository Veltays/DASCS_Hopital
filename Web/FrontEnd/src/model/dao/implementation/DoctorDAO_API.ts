import type { DoctorAccessLayer } from '../DoctorAccessLayer.ts'
import type { Doctor } from '../../entity/Doctor.ts'
import type { DoctorVM } from '../../viewmodel/DoctorVM.ts'

// Exception personnalisée pour l'absence de Doctor
export class DoctorsNotFoundError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class DoctorDAO_API implements DoctorAccessLayer {
  private selectedDoctors: Array<Doctor>
  private API_ENDPOINT: string = '/api/doctors'

  constructor() {
    this.selectedDoctors = []
  }

  public getList(): Array<Doctor> {
    return this.selectedDoctors
  }

  public async load(vm?: DoctorVM): Promise<Array<Doctor>> {
    this.selectedDoctors = []

    if (vm) {
      const params = new URLSearchParams()

      if (vm.id) {
        params.append('id', String(vm.id))
      }
      if (vm.lastname) {
        params.append('name', vm.lastname)
      }
      if (vm.specialtyId) {
        params.append('specialty', String(vm.specialtyId))
      }
      if (vm.userId) {
        params.append('userId', String(vm.userId))
      }

      const res = await fetch(`${this.API_ENDPOINT}?${params.toString()}`)

      if (res.ok) {
        this.selectedDoctors = await res.json()
      } else {
        throw new DoctorsNotFoundError('Erreur de load')
      }
    } else {
      const res = await fetch(`${this.API_ENDPOINT}`)

      if (res.ok) {
        this.selectedDoctors = await res.json()
      } else {
        throw new DoctorsNotFoundError('Erreur de load')
      }
    }

    return this.selectedDoctors
  }

  public async save(doctorToSave: Doctor): Promise<void> {
    if (doctorToSave.id == null) {
      // ajout
      const newDoctor: Doctor = {
        id: -1,
        firstname: doctorToSave.firstname,
        lastname: doctorToSave.lastname,
        specialtyId: doctorToSave.specialtyId,
        userId: doctorToSave.userId,
      }

      const res = await fetch(this.API_ENDPOINT, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newDoctor),
      })

      const doctorAdded = await res.json()
      console.log(doctorAdded)
    } else {
      // modification
      await fetch(`${this.API_ENDPOINT}/${doctorToSave.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(doctorToSave),
      })
    }
  }

  public async delete(item: null | string | Doctor): Promise<void> {
    if (item == null) {
      throw new DoctorsNotFoundError('Erreur de delete: paramètre nul')
    }

    let id: string

    if (typeof item === 'string') {
      id = item
    } else if (item.id) {
      id = String(item.id)
    } else {
      throw new DoctorsNotFoundError('Erreur de delete: Doctor sans id')
    }

    const res = await fetch(`${this.API_ENDPOINT}/${id}`, {
      method: 'DELETE',
    })

    if (!res.ok) {
      throw new DoctorsNotFoundError(`Doctor avec id ${id} pas trouvé`)
    }

    console.log(`Doctor ${id} supprimé avec succès`)
  }
}
