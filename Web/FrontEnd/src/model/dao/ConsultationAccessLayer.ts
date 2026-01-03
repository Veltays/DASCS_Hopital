import type { Consultation } from '@/model/entity/Consultation.ts'
import type { ConsultationVM } from '@/model/viewmodel/ConsultationVM'

export interface ConsultationAccessLayer {
  load(vm?: ConsultationVM): Promise<Consultation[]>
  getList(): Consultation[]
  save(consultation: Consultation): Promise<void>
  delete(item: string | Consultation | null): Promise<void>
}
