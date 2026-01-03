import type { ConsultationAccessLayer } from '../ConsultationAccessLayer.ts'
import type { Consultation } from '../../entity/Consultation.ts'
import type { ConsultationVM } from '../../viewmodel/ConsultationVM.ts'

// Exception personnalisée pour l'absence de Consultation
export class ConsultationsNotFoundError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class ConsultationDAO_API implements ConsultationAccessLayer {
  private selectedConsultations: Array<Consultation>
  private API_ENDPOINT: string = 'http://localhost:8081/api/consultations'

  constructor() {
    this.selectedConsultations = []
  }

  public getList(): Array<Consultation> {
    return this.selectedConsultations
  }

  // ===== LOAD (MÊME STRUCTURE QUE SPECIALTY) =====
  public async load(vm?: ConsultationVM): Promise<Array<Consultation>> {
    this.selectedConsultations = []

    if (vm) {
      const params = new URLSearchParams()

      if (vm.id) {
        params.append('id', String(vm.id))
      }
      if (vm.doctorId) {
        params.append('doctorId', String(vm.doctorId))
      }
      if (vm.patientId) {
        params.append('patientId', String(vm.patientId))
      }
      if (vm.date) {
        params.append('date', vm.date)
      }

      const res = await fetch(`${this.API_ENDPOINT}?${params.toString()}`)

      if (res.ok) {
        this.selectedConsultations = await res.json()
      } else {
        throw new ConsultationsNotFoundError('Erreur de load')
      }
    } else {
      const res = await fetch(`${this.API_ENDPOINT}`)

      if (res.ok) {
        this.selectedConsultations = await res.json()
      } else {
        throw new ConsultationsNotFoundError('Erreur de load')
      }
    }

    return this.selectedConsultations
  }

  // ===== SAVE (POST / PUT EXACTEMENT COMME SPECIALTY) =====
  public async save(consultationToSave: Consultation): Promise<void> {
    if (consultationToSave.id == null) {
      // ajout
      const newConsultation: Consultation = {
        id: -1,
        doctorId: consultationToSave.doctorId,
        patientId: consultationToSave.patientId,
        date: consultationToSave.date,
        hour: consultationToSave.hour,
        duration: consultationToSave.duration,
        reason: consultationToSave.reason,
      }

      const res = await fetch(this.API_ENDPOINT, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newConsultation),
      })

      const consultationAdded = await res.json()
      console.log(consultationAdded)
    } else {
      // modification
      await fetch(`${this.API_ENDPOINT}/${consultationToSave.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(consultationToSave),
      })
    }
  }

  // ===== DELETE (DELETE EXACTEMENT COMME SPECIALTY) =====
  public async delete(item: null | string | Consultation): Promise<void> {
    if (item == null) {
      throw new ConsultationsNotFoundError('Erreur de delete: paramètre nul')
    }

    let id: string

    if (typeof item === 'string') {
      id = item
    } else if (item.id) {
      id = String(item.id)
    } else {
      throw new ConsultationsNotFoundError('Erreur de delete: Consultation sans id')
    }

    const res = await fetch(`${this.API_ENDPOINT}/${id}`, {
      method: 'DELETE',
    })

    if (!res.ok) {
      throw new ConsultationsNotFoundError(`Consultation avec id ${id} pas trouvée`)
    }

    console.log(`Consultation ${id} supprimée avec succès`)
  }
}
