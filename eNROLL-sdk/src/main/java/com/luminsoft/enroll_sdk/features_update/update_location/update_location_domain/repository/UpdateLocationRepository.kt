
import arrow.core.Either
import arrow.core.raise.Null
import com.luminsoft.enroll_sdk.core.failures.SdkFailure
import com.luminsoft.enroll_sdk.features.location.location_data.location_models.get_token.PostLocationRequestModel

interface UpdateLocationRepository {
    suspend fun updateLocation(request: UpdateLocationRequestModel): Either<SdkFailure, Null>
}