package onextent.akka.demo.sangria.models

import java.util.{Date, UUID}

final case class Message(
    id: UUID,
    datetime: Date,
    body: String
)

