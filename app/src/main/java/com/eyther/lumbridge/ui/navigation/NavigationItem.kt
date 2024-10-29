package com.eyther.lumbridge.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
abstract class NavigationItem {
    abstract val route: String
    abstract val label: Int

    /**
     * Optional method to build a route with arguments.
     * @param args the arguments to build the route with
     */
    open fun buildRouteWithArgs(vararg args: Any): String {
        val routeWithoutArgs = route.substring(0, route.indexOf("/"))
        return "$routeWithoutArgs/${args.joinToString("/")}"
    }
}
