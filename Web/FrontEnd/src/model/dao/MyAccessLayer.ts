import type {ToDo} from '../entity/Speciality'
import type {ToDoVM} from '../viewmodel/ToDoVM'

export interface SpecialtiesAccessLayer {
  load(toDoVM?: ToDoVM): Promise<Array<ToDo>>
  getList(): Array<ToDo>
  save(toDo: ToDo): Promise<void>
  delete(item: string | ToDo): Promise<void>
}
