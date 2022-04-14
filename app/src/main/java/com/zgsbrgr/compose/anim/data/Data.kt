package com.zgsbrgr.compose.anim.data

import com.zgsbrgr.compose.anim.R


val individualSports = listOf<SportsData>(
    SportsData(
        1,
        "downhill skiing",
        "individual",
        R.drawable.downhill_skiing_black_24dp
    ),
    SportsData(
        2,
        "nordic walking",
        "individual",
        R.drawable.nordic_walking_black_24dp
    ),
    SportsData(
        3,
        "martial arts",
        "individual",
        R.drawable.sports_martial_arts_black_24dp
    ),
    SportsData(
        4,
        "mma",
        "individual",
        R.drawable.sports_mma_black_24dp
    ),
    SportsData(
        5,
        "gymnastics",
        "individual",
        R.drawable.sports_gymnastics_black_24dp
    ),
    SportsData(
        6,
        "snowshoeing",
        "individual",
        R.drawable.snowshoeing_black_24dp
    )
)

val extremeSports = listOf<SportsData>(
    SportsData(
        7,
        "surfing",
        "extreme",
        R.drawable.surfing_white_24dp
    ),
    SportsData(
        8,
        "kitesurfing",
        "extreme",
        R.drawable.kitesurfing_white_24dp
    ),
    SportsData(
        9,
        "paragliding",
        "extreme",
        R.drawable.paragliding_black_24dp
    ),
    SportsData(
        10,
        "roller skating",
        "extreme",
        R.drawable.roller_skating_black_24dp
    ),
    SportsData(
        11,
        "skateboarding",
        "extreme",
        R.drawable.skateboarding_black_24dp
    ),
    SportsData(
        12,
        "snowboarding",
        "extreme",
        R.drawable.snowboarding_black_24dp
    )

)

val partnerSports = listOf<SportsData>(
    SportsData(
        13,
        "ice skating",
        "partner",
        R.drawable.ice_skating_black_24dp
    ),
    SportsData(
        14,
        "golf",
        "partner",
        R.drawable.sports_golf_black_24dp
    ),
    SportsData(
        15,
        "kayaking",
        "partner",
        R.drawable.kayaking_black_24dp
    ),
    SportsData(
        16,
        "rowing",
        "partner",
        R.drawable.rowing_white_24dp
    ),
    SportsData(
        17,
        "volley ball",
        "partner",
        R.drawable.sports_volleyball_black_24dp
    ),
    SportsData(
        18,
        "scuba diving",
        "partner",
        R.drawable.scuba_diving_white_24dp
    )
)

val teamSports = listOf<SportsData>(
    SportsData(
        19,
        "basketball",
        "team",
        R.drawable.sports_basketball_black_24dp
    ),
    SportsData(
        20,
        "cricket",
        "team",
        R.drawable.sports_cricket_black_24dp
    ),
    SportsData(
        21,
        "football",
        "team",
        R.drawable.sports_football_black_24dp
    ),
    SportsData(
        22,
        "handball",
        "team",
        R.drawable.sports_handball_black_24dp
    ),
    SportsData(
        23,
        "hockey",
        "team",
        R.drawable.sports_hockey_black_24dp
    ),
    SportsData(
        24,
        "soccer",
        "team",
        R.drawable.sports_soccer_black_24dp
    ),
)

val sports = listOf<SportsCategory>(
    SportsCategory(
        1,
        "individual",
        1,
        individualSports
    ),
    SportsCategory(
        2,
        "partner",
        2,
        partnerSports
    ),
    SportsCategory(
        3,
        "team",
        3,
        teamSports
    ),
    SportsCategory(
        4,
        "extreme",
        4,
        extremeSports
    ),

)


data class SportsCategory(
    val categoryId: Int,
    val name: String,
    val icon: Int,
    val sports: List<SportsData>
)

data class SportsData(
    val id: Int,
    val name: String,
    val category: String,
    val icon: Int
)