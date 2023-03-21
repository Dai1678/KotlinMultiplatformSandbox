package dev.dai.kotlinmultiplatformsandbox.shared

import dev.dai.kotlinmultiplatformsandbox.entity.RocketLaunch
import dev.dai.kotlinmultiplatformsandbox.shared.cache.Database
import dev.dai.kotlinmultiplatformsandbox.shared.cache.DatabaseDriverFactory
import dev.dai.kotlinmultiplatformsandbox.shared.netowrk.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class)
    suspend fun getLaunches(forceReloaded: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReloaded) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}