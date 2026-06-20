package com.example.data

data class Post(
    val id: String = "",
    val title: String = "",
    val shortDesc: String = "",
    val fullDesc: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    // Map conversion for Firestore
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "title" to title,
            "shortDesc" to shortDesc,
            "fullDesc" to fullDesc,
            "timestamp" to timestamp
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Post {
            return Post(
                id = map["id"] as? String ?: "",
                title = map["title"] as? String ?: "",
                shortDesc = map["shortDesc"] as? String ?: "",
                fullDesc = map["fullDesc"] as? String ?: "",
                timestamp = (map["timestamp"] as? Number)?.toLong() ?: 0L
            )
        }
    }
}

data class ShopDetails(
    val shopName: String = "Puja Online Cafe",
    val ownerName: String = "Gopal Mandal",
    val shopLocation: String = "Jendur Nearby Electricity Transformer",
    val mobileNumber: String = "+919832223242",
    val whatsappNumber: String = "+919832223242",
    val shareLink: String = "https://play.google.com/store/apps/details?id=com.example.pujaonlinecafe"
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "shopName" to shopName,
            "ownerName" to ownerName,
            "shopLocation" to shopLocation,
            "mobileNumber" to mobileNumber,
            "whatsappNumber" to whatsappNumber,
            "shareLink" to shareLink
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): ShopDetails {
            return ShopDetails(
                shopName = map["shopName"] as? String ?: "Puja Online Cafe",
                ownerName = map["ownerName"] as? String ?: "Gopal Mandal",
                shopLocation = map["shopLocation"] as? String ?: "Jendur Nearby Electricity Transformer",
                mobileNumber = map["mobileNumber"] as? String ?: "+919832223242",
                whatsappNumber = map["whatsappNumber"] as? String ?: "+919832223242",
                shareLink = map["shareLink"] as? String ?: "https://play.google.com/store/apps/details?id=com.example.pujaonlinecafe"
            )
        }
    }
}

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val whatsapp: String = "",
    val isAdmin: Boolean = false,
    val isBanned: Boolean = false
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "whatsapp" to whatsapp,
            "isAdmin" to isAdmin,
            "isBanned" to isBanned
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): UserProfile {
            return UserProfile(
                uid = map["uid"] as? String ?: "",
                name = map["name"] as? String ?: "",
                email = map["email"] as? String ?: "",
                whatsapp = map["whatsapp"] as? String ?: "",
                isAdmin = map["isAdmin"] as? Boolean ?: false,
                isBanned = map["isBanned"] as? Boolean ?: false
            )
        }
    }
}
