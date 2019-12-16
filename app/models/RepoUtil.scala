package models

import cats.effect.IO
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object RepoUtil {
  def fromFuture[IO[A], A](f: => Future[A])(implicit ec: ExecutionContext): cats.effect.IO[A] =
    IO.delay(f) >>= (
      f => IO.async[A] { cb =>
        f.onComplete {
          case Success(a) => cb(Right(a))
          case Failure(ex) => cb(Left(ex))
        }
      })
}
