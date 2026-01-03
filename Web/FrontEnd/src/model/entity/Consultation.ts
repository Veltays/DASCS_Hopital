export interface Consultation {
  id?: number
  doctorId: number
  patientId: number
  hour: string
  date: string
  duration: string
  reason: string
}
