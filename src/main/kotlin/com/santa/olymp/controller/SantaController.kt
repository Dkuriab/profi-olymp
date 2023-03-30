package com.santa.olymp.controller

import com.santa.olymp.dp.SantaBDImpl
import com.santa.olymp.dto.GroupParamsDTO
import com.santa.olymp.dto.ParticipantInfoDTO
import com.santa.olymp.model.Group
import com.santa.olymp.model.Participant
import org.springframework.web.bind.annotation.*


@RestController
class SantaController {
    private val santaDB = SantaBDImpl()

    @GetMapping("/groups")
    fun groups(): List<Group> =
            santaDB.getAllGroups()


    @PostMapping("/groups")
    fun postGroup(group: Group): Long =
            santaDB.addGroup(group)

    @GetMapping("/group/{id}")
    fun getGroupById(@PathVariable id: Long): Group =
            santaDB.getGroup(id = id)

    /**
     * Редактирование группы по идентификатору группы
     * Редактировать можно только свойства name, description
     * Удалить название таким образом нельзя, описание – можно
     */
    @PutMapping("/group/{id}")
    fun updateGroup(@PathVariable id: Long, @RequestBody newParams: GroupParamsDTO) {
        santaDB.updateGroup(id = id, newParams = newParams)
    }

    /**
     * Удаление группы по идентификатору
     */
    @DeleteMapping("/group/{id}")
    fun deleteGroup(@PathVariable id: Long) = santaDB.deleteGroup(id)


    /**
     * Добавление участника в группу по идентификатору группы
     * Пожелания – не обязательный параметр, имя – обязательный
     */
    @PostMapping("/group/{id}/participant")
    fun addParticipant(@PathVariable id: Long, @RequestBody user: ParticipantInfoDTO) =
        santaDB.addUserToTheGroup(groupId = id, user = user)


    /**
     * Удаление участника из группы по идентификаторам группы и участника
     */
    @DeleteMapping("/group/{groupId}/participant/{participantId}")
    fun deleteParticipantFromGroup(@PathVariable groupId: Long, @PathVariable participantId: Long) {
        santaDB.deleteParticipant(groupId = groupId, participantId = participantId)
    }

    /**
     * Проведение жеребьевки в группе по идентификатору группы
     * Проведение жеребьевки возможно только в том случае, когда количество участников группы >= 3
     * Участнику в качестве подопечного нельзя выдать самого себя
     * Участник не может быть подопечным одновременно у двух и более участников
     */
    @PostMapping("/group/{id}/toss")
    fun tossGroup(groupId: Long): List<Participant> =
        santaDB.tossGroup(groupId = groupId)


    /**
     * Получение информации для конкретного участника группы, кому он дарит подарок.
     */
    @GetMapping("/group/{groupId}/participant/{participantId}/recipient")
    fun getRecipient(@PathVariable groupId: Long, @PathVariable participantId: Long) =
            santaDB.getRecipient(groupId, participantId)
}