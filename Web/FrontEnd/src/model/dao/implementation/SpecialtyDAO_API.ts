import type {SpecialtiesAccessLayer} from "../SpecialtyAccessLayer.ts"
import type {Specialty} from "../../entity/Speciality.ts";
import type {SpecialtyVM} from "../../viewmodel/SpecialtyVM.ts";


// Exception personnalisée pour l'absence de Specialty
export class SpecialtiesNotFoundError extends Error {
  constructor(message: string) {
    super(message);
  }
}



export class SpecialtyDAO_API implements SpecialtiesAccessLayer {
  private selectedSpecialities: Array<Specialty>
  private API_ENDPOINT: string = '/api/specialties'
  constructor() {
    this.selectedSpecialities = []
  }

  public getList(): Array<Specialty> {
    return this.selectedSpecialities
  }

  public async load(SpecialtyVM?: SpecialtyVM): Promise<Array<Specialty>> {
    this.selectedSpecialities = []
    if (SpecialtyVM) {
      const params = new URLSearchParams()
      if (SpecialtyVM.id) {
        params.append('id', String(SpecialtyVM.id))
      }
      if (SpecialtyVM.name) {
        params.append('name', SpecialtyVM.name)
      }

      const res = await fetch(`${this.API_ENDPOINT}?${params.toString()}`)

      if (res.ok) {
        this.selectedSpecialities = await res.json()
      } else {
        throw new SpecialtiesNotFoundError('Erreur de load')
      }
    } else {
      const res = await fetch(`${this.API_ENDPOINT}`)
      if (res.ok) {
        this.selectedSpecialities = await res.json()
      } else {
        throw new SpecialtiesNotFoundError('Erreur de load')
      }
    }
    return this.selectedSpecialities
  }

  public async save(SpecialtyToSave: Specialty): Promise<void> {
    if (SpecialtyToSave.id == null) {
      // ajout
      const newSpecialty: Specialty = {
        id: -1,
        name: SpecialtyToSave.name,
      }
      const res = await fetch(this.API_ENDPOINT, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newSpecialty),
      })
      const SpecialtyAdded = await res.json()
      console.log(SpecialtyAdded)
    } else {
      //modification
      await fetch(`${this.API_ENDPOINT}/${SpecialtyToSave.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(SpecialtyToSave),
      })
    }
  }
  public async delete(item: null | string | Specialty): Promise<void> {
    if (item == null) {
      throw new SpecialtiesNotFoundError(`Erreur de delete: paramètre nul`)
    }

    let id: string

    if (typeof item === 'string') {
      id = item
    } else if (item.id) {
      id = String(item.id)
    } else {
      throw new SpecialtiesNotFoundError(`Erreur de delete: Specialty sans id`)
    }

    const res = await fetch(`${this.API_ENDPOINT}/${id}`, {
      method: 'DELETE',
    })

    if (!res.ok) {
      throw new SpecialtiesNotFoundError(`Specialty avec id ${id} pas trouvée`)
    }

    console.log(`Specialty ${id} supprimée avec succès`)
  }
}
