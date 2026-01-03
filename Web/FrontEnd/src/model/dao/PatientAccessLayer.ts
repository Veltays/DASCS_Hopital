import type { Patient } from '@/model/entity/Patient'
import type { PatientVM } from '@/model/viewmodel/PatientVM'

export interface PatientAccessLayer {
  load(vm?: PatientVM): Promise<Patient[]>
  getList(): Patient[]
  save(patient: Patient,newPatient: boolean): Promise<number>
  delete(item: string | Patient | null): Promise<void>
}
