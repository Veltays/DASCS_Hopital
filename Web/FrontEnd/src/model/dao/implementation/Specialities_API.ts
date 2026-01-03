import type {SpecialtiesAccessLayer} from "../MyAccessLayer.ts"
import type {Speciality} from "../../entity/Speciality.ts";
import type {ToDoVM} from "../viewmodel/ToDoVM.ts";


// Exception personnalisée pour l'absence de ToDo
export class SpecialtiesNotFoundError extends Error {
  constructor(message: string) {
    super(message);
  }
}



export class ToDoDAO_API implements SpecialtiesAccessLayer {
  private selectedSpecialities: Array<Speciality>
  private API_ENDPOINT: string = "http://localhost:8081/api";
  constructor() {
    this.selectedSpecialities = [];
  }

  public getList(): Array<Speciality> {
    return this.selectedSpecialities;
  }


  public async load(toDoVM?: ToDoVM): Promise<Array<Speciality>> {
    this.selectedSpecialities = []
    if (toDoVM) {
      const params = new URLSearchParams();
      if (toDoVM.text) {
        params.append('text', toDoVM.text)
      }
      if (toDoVM.id) {
        params.append('id', toDoVM.id)
      }
      if (typeof toDoVM.priority === 'number') {
        params.append('priority',toDoVM.priority.toString())
      }
      if (typeof toDoVM.completed === 'boolean') {
        params.append('completed', toDoVM.completed.toString())
      }
      const res = await fetch(`${this.API_ENDPOINT}?${params.toString()}`);
      if (res.ok) {
        this.selectedSpecialities = await res.json()
      } else {
        throw new SpecialtiesNotFoundError("Erreur de load")
      }
    } else {
      const res = await fetch(`${this.API_ENDPOINT}`)
      if (res.ok) {
        this.selectedSpecialities = await res.json()
      } else {
        throw new SpecialtiesNotFoundError("Erreur de load")
      }
    }
    return this.selectedSpecialities
  }
  public async save(toDo: Speciality): Promise<void> {
    if (toDo.id == null) { // ajout
      const newToDo: ToDo = {
        text: toDo.text,
        priority: toDo.priority,
        completed: toDo.completed
      }
      const res = await fetch(this.API_ENDPOINT, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newToDo)
      })
      const addedToDo = await res.json()
      console.log(addedToDo)
    } else { // modification
      const res = await fetch(`${this.API_ENDPOINT}/${toDo.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(toDo)
      })
    }
  }
  public async delete(item: null | string | Speciality): Promise<void> {
    if (item == null) {
      throw new SpecialtiesNotFoundError(`Erreur de delete: paramètre nul`);
    }
    let id: string
    if (typeof item === 'string') {
      id = item
    } else if (item.id) {
      id = item.id
    } else {
      throw new SpecialtiesNotFoundError(`Erreur de delete: ToDo sans id`);
    }
    const res = await fetch(`${this.API_ENDPOINT}/${id}`, {
      method: 'DELETE'
    })
    if (!res.ok) {
      throw new SpecialtiesNotFoundError(`ToDo avec id ${id} pas trouvé !`);
    }
    console.log(`Tâche ${id} supprimée avec succès`);
  }
}
