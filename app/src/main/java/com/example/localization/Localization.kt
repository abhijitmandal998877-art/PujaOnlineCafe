package com.example.localization

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    BENGALI("bn", "বাংলা"),
    HINDI("hi", "हिन्दी")
}

object Localization {
    private val translations = mapOf(
        "app_name" to mapOf(
            "en" to "Puja Online Cafe",
            "bn" to "পূজা অনলাইন ক্যাফে",
            "hi" to "पूजा ऑनलाइन कैफ़े"
        ),
        "dashboard" to mapOf(
            "en" to "Dashboard",
            "bn" to "ড্যাশবোর্ড",
            "hi" to "डैशबोर्ड"
        ),
        "contact" to mapOf(
            "en" to "Contact",
            "bn" to "যোগাযোগ",
            "hi" to "संपर्क"
        ),
        "settings" to mapOf(
            "en" to "Settings",
            "bn" to "সেটিংস",
            "hi" to "सेटिंग्स"
        ),
        "login" to mapOf(
            "en" to "Login",
            "bn" to "লগইন",
            "hi" to "लॉगिन"
        ),
        "signup" to mapOf(
            "en" to "Sign Up",
            "bn" to "নিবন্ধন করুন",
            "hi" to "साइन अप"
        ),
        "email" to mapOf(
            "en" to "Email Address",
            "bn" to "ইমেল ঠিকানা",
            "hi" to "ईमेल पता"
        ),
        "whatsapp" to mapOf(
            "en" to "WhatsApp Number",
            "bn" to "হোয়াটসঅ্যাপ নম্বর",
            "hi" to "व्हाट्सएप नंबर"
        ),
        "password" to mapOf(
            "en" to "Password",
            "bn" to "পাসওয়ার্ড",
            "hi" to "पासवर्ड"
        ),
        "name" to mapOf(
            "en" to "Full Name",
            "bn" to "পুরো নাম",
            "hi" to "पूरा नाम"
        ),
        "sign_in_with_google" to mapOf(
            "en" to "Sign In with Google",
            "bn" to "গুগল দিয়ে লগইন",
            "hi" to "गूगल से साइन इन"
        ),
        "loading_animation" to mapOf(
            "en" to "Opening Puja Online Cafe...",
            "bn" to "পূজা অনলাইন ক্যাফে খোলা হচ্ছে...",
            "hi" to "पूजा ऑनलाइन कैफ़े खोला जा रहा है..."
        ),
        "logout" to mapOf(
            "en" to "Logout",
            "bn" to "লগ আউট",
            "hi" to "লॉग आउट"
        ),
        "day_night_toggle" to mapOf(
            "en" to "Dark Mode",
            "bn" to "ডার্ক মোড",
            "hi" to "डार्क मोड"
        ),
        "language" to mapOf(
            "en" to "Language",
            "bn" to "ভাষা",
            "hi" to "भाषा"
        ),
        "current_work" to mapOf(
            "en" to "Current Online Works",
            "bn" to "চলতি অনলাইন কাজসমূহ",
            "hi" to "वर्तमान ऑनलाइन कार्य"
        ),
        "no_posts" to mapOf(
            "en" to "No posts available. Stay tuned!",
            "bn" to "কোনও পোস্ট নেই। সাথে থাকুন!",
            "hi" to "कोई पोस्ट उपलब्ध नहीं है। बने रहें!"
        ),
        "contact_owner" to mapOf(
            "en" to "Shop Owner",
            "bn" to "দোকানের মালিক",
            "hi" to "दुकान मालिक"
        ),
        "contact_location" to mapOf(
            "en" to "Location",
            "bn" to "ঠিকানা",
            "hi" to "पता"
        ),
        "call_now" to mapOf(
            "en" to "Call Owner",
            "bn" to "কল করুন",
            "hi" to "कॉल करें"
        ),
        "chat_on_whatsapp" to mapOf(
            "en" to "Chat on WhatsApp",
            "bn" to "হোয়াটসঅ্যাপ চ্যাট",
            "hi" to "व्हाट्सएप पर चैट करें"
        ),
        "banned_title" to mapOf(
            "en" to "Access Revoked",
            "bn" to "প্রবেশাধিকার বাতিল",
            "hi" to "पहुंच रद्द"
        ),
        "banned_message" to mapOf(
            "en" to "You have been banned from using this application. If you think this is a mistake, please contact the administrator.",
            "bn" to "এই অ্যাপ্লিকেশন ব্যবহার করা থেকে আপনাকে ব্যান করা হয়েছে। যদি মনে করেন এটি ভুলবশত হয়েছে, তবে প্রশাসকের সাথে যোগাযোগ করুন।",
            "hi" to "आपको इस एप्लिकेशन का उपयोग करने से बैन कर दिया गया है। यदि आपको लगता है कि यह कोई त्रुटि है, तो प्रशासक से संपर्क करें।"
        ),
        "banned_contact" to mapOf(
            "en" to "Contact Details",
            "bn" to "যোগাযোগের বিবরণ",
            "hi" to "संपर्क विवरण"
        ),
        "admin_panel" to mapOf(
            "en" to "Admin Panel",
            "bn" to "অ্যাডমিন প্যানেল",
            "hi" to "प्रशासक पैनल"
        ),
        "publish_post" to mapOf(
            "en" to "Publish Post",
            "bn" to "পোস্ট প্রকাশ করুন",
            "hi" to "पोस्ट प्रकाशित करें"
        ),
        "edit_post" to mapOf(
            "en" to "Edit Post",
            "bn" to "পোস্ট সম্পাদনা",
            "hi" to "पोस्ट संपादित करें"
        ),
        "delete_post" to mapOf(
            "en" to "Delete Post",
            "bn" to "পোস্ট মুছুন",
            "hi" to "पोस्ट हटाएं"
        ),
        "post_title" to mapOf(
            "en" to "Title",
            "bn" to "শিরোনাম",
            "hi" to "शीर्षक"
        ),
        "post_short_desc" to mapOf(
            "en" to "Short Description",
            "bn" to "ছোট বিবরণ",
            "hi" to "संक्षिप्त विवरण"
        ),
        "post_full_desc" to mapOf(
            "en" to "Full Content",
            "bn" to "সম্পূর্ণ বিষয়বস্তু",
            "hi" to "पूर्ण सामग्री"
        ),
        "shop_location" to mapOf(
            "en" to "Shop Location",
            "bn" to "দোকানের ঠিকানা",
            "hi" to "दुकान का पता"
        ),
        "shop" to mapOf(
            "en" to "Shop Details",
            "bn" to "দোকানের তথ্য",
            "hi" to "दुकान विवरण"
        ),
        "save_changes" to mapOf(
            "en" to "Save Changes",
            "bn" to "পরিবর্তন সংরক্ষণ করুন",
            "hi" to "बदलाव सहेजें"
        ),
        "manage_customers" to mapOf(
            "en" to "Manage Customers",
            "bn" to "গ্রাহক পরিচালনা",
            "hi" to "ग्राहकों का प्रबंधन"
        ),
        "ban" to mapOf(
            "en" to "Ban",
            "bn" to "ব্যান করুন",
            "hi" to "बैन करें"
        ),
        "unban" to mapOf(
            "en" to "Unban",
            "bn" to "ব্যান তুলে নিন",
            "hi" to "बैन हटाएं"
        ),
        "customer_banned" to mapOf(
            "en" to "Customer Banned Successfully",
            "bn" to "গ্রাহক ব্যান করা হয়েছে",
            "hi" to "ग्राहक को बैन कर दिया गया"
        ),
        "customer_unbanned" to mapOf(
            "en" to "Customer Unbanned",
            "bn" to "গ্রাহকের ব্যান সরানো হয়েছে",
            "hi" to "ग्राहक बैन मुक्त कर दिया गया"
        ),
        "post_published" to mapOf(
            "en" to "Post Published Successfully",
            "bn" to "পোস্ট সফলভাবে প্রকাশিত হয়েছে",
            "hi" to "पोस्ट सफलतापूर्वक प्रकाशित किया गया"
        ),
        "post_deleted" to mapOf(
            "en" to "Post Deleted Successfully",
            "bn" to "পোস্ট সফলভাবে মোছা হয়েছে",
            "hi" to "पोस्ट सफलतापूर्वक हटा दिया गया"
        ),
        "welcome" to mapOf(
            "en" to "Welcome",
            "bn" to "স্বাগতম",
            "hi" to "स्वागत है"
        ),
        "notification_bell" to mapOf(
            "en" to "Notifications",
            "bn" to "বিজ্ঞপ্তি",
            "hi" to "सूचनाएं"
        ),
        "new_work_notification" to mapOf(
            "en" to "New post added! Go check details on Dashboard.",
            "bn" to "নতুন পোস্ট যুক্ত হয়েছে! ড্যাশবোর্ডে গিয়ে বিবরণ দেখুন।",
            "hi" to "नया पोस्ट जुड़ा! डैशबोर्ड पर जाकर विवरण देखें।"
        ),
        "whats_app_or_email" to mapOf(
            "en" to "Email or WhatsApp Number",
            "bn" to "ইমেল বা হোয়াটসঅ্যাপ নম্বর",
            "hi" to "ईमेल या व्हाट्सएप नंबर"
        ),
        "password_mismatch" to mapOf(
            "en" to "Passwords cannot be empty",
            "bn" to "পাসওয়ার্ড খালি হতে পারবে না",
            "hi" to "पासवर्ड खाली नहीं होना चाहिए"
        ),
        "fill_all_fields" to mapOf(
            "en" to "Please fill in all requested fields",
            "bn" to "দয়া করে সকল তথ্য পূরণ করুন",
            "hi" to "कृपया सभी फ़ील्ड भरें"
        ),
        "dont_have_account" to mapOf(
            "en" to "Don't have an account? Sign Up",
            "bn" to "অ্যাকাউন্ট নেই? সাইন আপ করুন",
            "hi" to "खाता नहीं है? साइन अप करें"
        ),
        "already_have_account" to mapOf(
            "en" to "Already have an account? Login",
            "bn" to "ইতিমধ্যে অ্যাকাউন্ট আছে? লগইন করুন",
            "hi" to "पहले से खाता है? लॉगिन करें"
        ),
        "go_to_full_post" to mapOf(
            "en" to "Read Full Post",
            "bn" to "বিস্তারিত পড়ুন",
            "hi" to "पूरा पोस्ट पढ़ें"
        ),
        "post_details" to mapOf(
            "en" to "Post Details",
            "bn" to "পোস্টের বিবরণ",
            "hi" to "पोस्ट विवरण"
        ),
        "edit" to mapOf(
            "en" to "Edit",
            "bn" to "সম্পাদনা",
            "hi" to "संपादित करें"
        ),
        "delete" to mapOf(
            "en" to "Delete",
            "bn" to "মুছুন",
            "hi" to "हटाएं"
        ),
        "share_app" to mapOf(
            "en" to "Share App",
            "bn" to "অ্যাপ শেয়ার করুন",
            "hi" to "ऐप शेयर करें"
        ),
        "link_copied" to mapOf(
            "en" to "Share link copied!",
            "bn" to "শেয়ার লিংক কপি করা হয়েছে!",
            "hi" to "शेयर लिंक कॉपी किया गया!"
        )
    )

    fun getString(key: String, language: AppLanguage): String {
        return translations[key]?.get(language.code) ?: (translations[key]?.get("en") ?: key)
    }
}
