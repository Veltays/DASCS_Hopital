import type { Specialty } from '../entity/Speciality'
import type { SpecialtyVM } from '@/model/viewmodel/SpecialtyVM.ts'

export interface SpecialtiesAccessLayer {
  load(SpecialtyVM?: SpecialtyVM): Promise<Array<Specialty>>
  getList(): Array<Specialty>
  save(toDo: Specialty): Promise<void>
  delete(item: string | Specialty): Promise<void>
}
