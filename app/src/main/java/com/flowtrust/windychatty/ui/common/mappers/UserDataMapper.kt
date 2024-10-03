package com.flowtrust.windychatty.ui.common.mappers

import com.flowtrust.windychatty.domain.models.Avatar
import com.flowtrust.windychatty.domain.models.Avatars
import com.flowtrust.windychatty.domain.models.ProfileData
import com.flowtrust.windychatty.domain.models.UserData
import com.flowtrust.windychatty.domain.models.UserDataToUpdate

fun mapProfileDataToUserDataToUpdate(profileData: ProfileData, avatar: Avatar):UserDataToUpdate {
    return UserDataToUpdate(
        avatar = avatar,
        birthday = profileData.birthday?:"",
        city = profileData.city?:"",
        instagram = profileData.instagram?:"",
        name = profileData.name,
        status = profileData.status?:"",
        username = profileData.username,
        vk = profileData.vk?:""
    )
}
