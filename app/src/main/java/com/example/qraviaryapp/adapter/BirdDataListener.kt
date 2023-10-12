import android.graphics.Bitmap

interface BirdDataListener {

    fun onBirdDataSaved(id: String?)

    fun onBirdDateSaved(birdData: BirdData)
}

interface ClickListener {
    fun onClick(nameValue: String)
    fun onClick(nameValue: String, id: String)
    fun onClick(nameValue: String, id: String, mutation: String)


}


data class BirdData(
    /*addBird*/
    var bitmap: Bitmap? = null,
    var imgUrl: String = "",
    var img: String? = null,
    var birdKey: String? = null,
    var flightKey: String? = null,
    var nurseryKey: String? = null,
    var id: String? = null,
    var legband: String? = null,
    var identifier: String? = null,
    var gender: String? = null,
    var mutation1: String? = null,
    var mutation2: String? = null,
    var mutation3: String? = null,
    var mutation4: String? = null,
    var mutation5: String? = null,
    var mutation6: String? = null,
    var dateOfBanding: String? = null,
    var dateOfBirth: String? = null,
    var status: String? = null,
    var availCage: String? = null,
    var forSaleCage: String? = null,
    var reqPrice: String? = null,
    var soldDate: String? = null,
    var soldPrice: String? = null,
    var saleContact: String? = null,
    var deathDate: String? = null,
    var deathReason: String? = null,
    var exDate: String? = null,
    var exReason: String? = null,
    var exContact: String? = null,
    var lostDate: String? = null,
    var lostDetails: String? = null,
    var donatedDate: String? = null,
    var donatedContact: String? = null,
    var otherComments: String? = null,
    var father: String? = null,
    var fatherKey: String? = null,
    var mother: String? = null,
    var motherKey: String? = null,
    var provenance: String? = null,
    var breederContact: String? = null,
    var buyPrice: String? = null,
    var boughtDate: String? = null,
    var otOtherContact: String? = null,
    var birdCount: String? = null,
    var cageKey: String? = null,
    var ageindays: String? = null,
    var cagekeyvalue: String? = null,
    var cagebirdkey: String? = null,
    var cagekeymalevalue: String? = null,
    var cagebirdmalekey: String? = null
)

data class CageData(
    var cage: String? = null,
    var cageId: String? = null,
    var cageCount: String? = null,
    var cageBirdsCount: String? = null,
    var cageBirdsMaturedCount: String? = null,
    var cageBirdsAvailCount: String? = null,
    var cagePairBirdCount: String? = null,
    var cageBirdCount: String? = null,
)

data class MutationData(
    var mutations: String? = null,
    var mutationsId: String? = null,
    var mutationCount: String? = null,
    var mutationsIncubateDays: String? = null,
    var mutationsMaturingDays: String? = null

)
data class DateTotalExpense(var date: String, var price: Double)
data class MonthYearEntry(val monthYearValue: Float, val amount: Float)
data class ExpensesData(
    var expenses: String? = null,
    var price: Double? =null,
    var date: Double? = null,
    var expensesId: String? = null,
    var expensesCount: String? = null,
    var expensesComment: String? = null,
    var expensesDate: String? = null,

)

data class PairData(
    var pairfemaleimg: String? = null,
    var pairmaleimg: String? = null,
    var pairKey: String? = null,
    var pairId: String? = null,
    var nest: String? = null,
    var pairCage: String? = null,
    var pairComment: String? = null,
    var pairDateBeg: String? = null,
    var pairDateSep: String? = null,
    var pairMale: String? = null,
    var pairFemale: String? = null,
    var pairFemaleKey: String? = null,
    var pairFlightFemaleKey: String? = null,
    var pairMaleKey: String? = null,
    var pairFlightMaleKey: String? = null,
    var pairMaleGender: String? = null,
    var pairFemaleGender: String? = null,
    var pairMaleMutation: String? = null,
    var pairFemaleMutation: String? = null,
    var paircagekeyFemale: String? = null,
    var paircagekeyMale: String? = null,
    var paircagebirdMale: String? = null,
    var paircagebirdFemale: String? = null,

)

data class EggData(
    var eggStatus: String? = null,
    var eggDate: String? = null,
    var movedate: String? = null,
    var pairKey: String? = null,
    var pairFlightMaleKey: String? = null,
    var pairFlightFemaleKey: String? = null,
    var pairBirdMaleKey: String? = null,
    var pairBirdFemaleKey: String? = null,
    var pairMaleId: String? = null,
    var pairFemaleId: String? = null,
    var eggKey: String? = null,
    var individualEggKey: String? = null,
    var eggCount: String? = null,
    var clutchCount: String? = null,
    var eggIncubating: String? = null,
    var eggLaid: String? = null,
    var eggHatched: String? = null,
    var eggNotFertilized: String? = null,
    var eggBroken: String? = null,
    var eggAbandon: String? = null,
    var eggDeadInShell: String? = null,
    var eggDeadBeforeMovingToNursery: String? = null,
    var eggLaidStartDate: String? = null,
    var eggIncubationStartDate: String? = null,
    var eggMaturingStartDate: String? = null,
    var eggcagekeyFemale: String? = null,
    var eggcagekeyMale: String? = null,
    var eggcagebirdMale: String? = null,
    var eggcagebirdFemale: String? = null,
    var birdkey: String? = null,
    var cage: String? = null,
    var datebirth: String? = null,
    var gender: String? = null,
    var identifier: String? = null,
    var legband: String? = null,
    var nurserykey: String? = null,
    var mutation1: String? = null,
    var mutation2: String? = null,
    var mutation3: String? = null,
    var mutation4: String? = null,
    var mutation5: String? = null,
    var mutation6: String? = null,
    var father: String? = null,
    var mother: String? = null,
    var fatherkey: String? = null,
    var fatherbirdkey: String? = null,
    var motherkey: String? = null,
    var motherbirdkey: String? = null,
    var status1: String? = null,
)
