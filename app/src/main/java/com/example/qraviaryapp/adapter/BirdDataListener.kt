interface BirdDataListener {

    fun onBirdDataSaved(id: String?)

    fun onBirdDateSaved(birdData: BirdData)
}

interface ClickListener{
    fun onClick(nameValue: String)
    fun onClick(nameValue: String, id: String)
    fun onClick(nameValue: String, id: String, mutation: String)

}



data class BirdData(
    /*addBird*/
    var imgUrl: String = "",
    var img: String? = null,
    var birdKey: String? = null,
    var flightKey: String? = null,
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
    var birdCount: String? = null
)

data class CageData(
    var cage: String? = null,
    var cageId: String? = null,
)

data class MutationData(
    var mutations: String? = null,
    var mutationsId: String? = null,
)
data class ExpensesData(
    var expenses: String? = null,
    var price: String? = null,
    var expensesId: String? = null,
    var expensesComment: String? = null,
    var expensesDate: String? = null
)
data class PairData(
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
    var pairMaleKey: String? = null,
    var pairMaleGender: String? = null,
    var pairFemaleGender: String? = null,
    var pairMaleMutation: String? = null,
    var pairFemaleMutation: String? = null
)

data class EggData(
    var eggStatus: String? = null,
    var eggDate: String? = null,
    var pairKey: String? = null,
    var eggKey: String? = null,
    var eggCount: String? = null,
    var eggIncubating: String? = null,
    var eggLaid: String? = null,
    var eggLaidStartDate: String? = null,
    var eggIncubationStartDate: String? = null,
    )
