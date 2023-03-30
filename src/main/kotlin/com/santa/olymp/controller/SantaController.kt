package com.santa.olymp.controller

import com.santa.olymp.dp.SantaBDImpl
import com.santa.olymp.dto.GroupDTO
import com.santa.olymp.dto.GroupInfoDTO
import com.santa.olymp.model.Group
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

//
//    PUT /group/{id}
//
//    DELETE /group/{id}
//
//    POST /group/{id}/parti cipant
//
//    DELETE /group/{groupId}/participant/{part icipantId}
//
//    POST /group/{id}/toss
//
//    GET  /group/{groupId} /participant/{part icipantId}/recipie nt
}