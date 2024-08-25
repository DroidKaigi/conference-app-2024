package io.github.droidkaigi.confsched.data.sponsors

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.NetworkService
import io.github.droidkaigi.confsched.data.sponsors.response.SponsorResponse
import io.github.droidkaigi.confsched.data.sponsors.response.SponsorsResponse
import io.github.droidkaigi.confsched.model.Plan
import io.github.droidkaigi.confsched.model.Sponsor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal interface SponsorApi {
    /**
     * Gets sponsor information for the DroidKaigi 2024 event.
     *
     * @return [SponsorsResponse]
     */
    @GET("/events/droidkaigi2024/sponsor")
    suspend fun getSponsors(): SponsorsResponse
}

public class DefaultSponsorsApiClient(
    private val networkService: NetworkService,
    ktorfit: Ktorfit,
) : SponsorsApiClient {

    private val sponsorApi = ktorfit.create<SponsorApi>()
    public override suspend fun sponsors(): PersistentList<Sponsor> {
        return networkService {
            sponsorApi.getSponsors()
        }.toSponsorList()
    }
}

public interface SponsorsApiClient {
    public suspend fun sponsors(): PersistentList<Sponsor>
}

private fun SponsorsResponse.toSponsorList(): PersistentList<Sponsor> {
    return sponsor.map { it.toSponsor() }.toPersistentList()
}

private fun SponsorResponse.toSponsor(): Sponsor {
    return Sponsor(
        name = sponsorName,
        logo = sponsorLogo,
        plan = Plan.ofOrNull(plan) ?: Plan.SUPPORTER,
        link = link,
    )
}
