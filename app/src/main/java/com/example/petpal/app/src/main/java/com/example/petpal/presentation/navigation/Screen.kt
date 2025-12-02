sealed class Screen(val route: String) {
    // ...existing code...

    // Order Screens
    object OrderForm : Screen("order_form/{serviceType}") {
        fun createRoute(serviceType: String) = "order_form/$serviceType"
    }
    object PetSelection : Screen("pet_selection")
    object TierSelection : Screen("tier_selection")
}

