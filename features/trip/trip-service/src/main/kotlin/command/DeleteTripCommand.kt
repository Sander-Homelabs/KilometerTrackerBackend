package command

import java.util.UUID

data class DeleteTripCommand(val groupId: UUID, val start: Int, val deletedBy: String)