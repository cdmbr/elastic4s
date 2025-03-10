package com.sksamuel.elastic4s.cats.effect.instances

import cats.effect.{Async, IO}
import cats.{Functor => CatsFunctor}
import com.sksamuel.elastic4s.cats.effect.CatsEffectExecutor
import com.sksamuel.elastic4s.{Executor, Functor}

trait CatsEffectInstances {
  implicit def catsFunctor[F[_]: CatsFunctor]: Functor[F] = new Functor[F] {
    override def map[A, B](fa: F[A])(f: A => B): F[B] = CatsFunctor[F].map(fa)(f)
  }

  implicit def catsEffectExecutor[F[_]: Async]: Executor[F] =
    new CatsEffectExecutor[F]

  // this needs to be at the bottom
  implicit val ioExecutor: Executor[IO] = new CatsEffectExecutor[IO]
}

@deprecated("Use CatsEffectInstances instead")
trait IOInstances extends CatsEffectInstances
