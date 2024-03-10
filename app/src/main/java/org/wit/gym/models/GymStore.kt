package org.wit.gym.models

interface GymStore {
    fun findAll(): List<GymModel>
    fun findAllFiltered(filter: String, callback: (List<GymModel>) -> Unit)
    fun create(gym: GymModel)
    fun delete(gym: GymModel)
    fun update(gym: GymModel)
}