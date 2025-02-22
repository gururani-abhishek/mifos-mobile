package org.mifos.mobile.api

import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.api.services.AuthenticationService
import org.mifos.mobile.api.services.BeneficiaryService
import org.mifos.mobile.api.services.ClientChargeService
import org.mifos.mobile.api.services.ClientService
import org.mifos.mobile.api.services.GuarantorService
import org.mifos.mobile.api.services.LoanAccountsListService
import org.mifos.mobile.api.services.NotificationService
import org.mifos.mobile.api.services.RecentTransactionsService
import org.mifos.mobile.api.services.RegistrationService
import org.mifos.mobile.api.services.SavingAccountsListService
import org.mifos.mobile.api.services.ThirdPartyTransferService
import org.mifos.mobile.api.services.UserDetailsService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 13/6/16
 */
class BaseApiManager @Inject constructor(preferencesHelper: PreferencesHelper) {
    val authenticationApi: AuthenticationService?
        get() = Companion.authenticationApi
    val clientsApi: ClientService
        get() = Companion.clientsApi
    val savingAccountsListApi: SavingAccountsListService?
        get() = Companion.savingAccountsListApi
    val loanAccountsListApi: LoanAccountsListService
        get() = Companion.loanAccountsListApi
    val recentTransactionsApi: RecentTransactionsService?
        get() = Companion.recentTransactionsApi
    val clientChargeApi: ClientChargeService?
        get() = Companion.clientChargeApi
    val beneficiaryApi: BeneficiaryService?
        get() = Companion.beneficiaryApi
    val thirdPartyTransferApi: ThirdPartyTransferService?
        get() = Companion.thirdPartyTransferApi
    val registrationApi: RegistrationService?
        get() = Companion.registrationApi
    val notificationApi: NotificationService
        get() = Companion.notificationApi
    val userDetailsService: UserDetailsService?
        get() = Companion.userDetailsService
    val guarantorApi: GuarantorService?
        get() = Companion.guarantorApi

    companion object {
        private var retrofit: Retrofit? = null
        private var authenticationApi: AuthenticationService? = null
        private lateinit var clientsApi: ClientService
        private var savingAccountsListApi: SavingAccountsListService? = null
        private lateinit var loanAccountsListApi: LoanAccountsListService
        private var recentTransactionsApi: RecentTransactionsService? = null
        private var clientChargeApi: ClientChargeService? = null
        private var beneficiaryApi: BeneficiaryService? = null
        private var thirdPartyTransferApi: ThirdPartyTransferService? = null
        private var registrationApi: RegistrationService? = null
        private lateinit var notificationApi: NotificationService
        private var userDetailsService: UserDetailsService? = null
        private var guarantorApi: GuarantorService? = null
        private fun init() {
            authenticationApi = createApi(AuthenticationService::class.java)
            clientsApi = createApi(ClientService::class.java)
            savingAccountsListApi = createApi(SavingAccountsListService::class.java)
            loanAccountsListApi = createApi(LoanAccountsListService::class.java)
            recentTransactionsApi = createApi(RecentTransactionsService::class.java)
            clientChargeApi = createApi(ClientChargeService::class.java)
            beneficiaryApi = createApi(BeneficiaryService::class.java)
            thirdPartyTransferApi = createApi(ThirdPartyTransferService::class.java)
            registrationApi = createApi(RegistrationService::class.java)
            notificationApi = createApi(NotificationService::class.java)
            guarantorApi = createApi(GuarantorService::class.java)
            userDetailsService = createApi(UserDetailsService::class.java)
        }

        private fun <T> createApi(clazz: Class<T>): T {
            return retrofit?.create(clazz)!!
        }

        @JvmStatic
        fun createService(endpoint: String?, tenant: String?, authToken: String?) {
            retrofit = Retrofit.Builder()
                .baseUrl(BaseURL().getUrl(endpoint!!))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(SelfServiceOkHttpClient(tenant, authToken).mifosOkHttpClient)
                .build()
            init()
        }
    }

    init {
        createService(
            preferencesHelper.baseUrl,
            preferencesHelper.tenant,
            preferencesHelper.token,
        )
    }
}
