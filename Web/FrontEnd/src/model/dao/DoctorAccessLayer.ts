import type { Doctor } from '@/model/entity/Doctor'
import type { DoctorVM } from '@/model/viewmodel/DoctorVM'

export interface DoctorAccessLayer {
  load(vm?: DoctorVM): Promise<Doctor[]>
  getList(): Doctor[]
  save(doctor: Doctor): Promise<void>
  delete(item: string | Doctor): Promise<void>
}
