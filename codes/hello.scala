import scala.io.StdIn.readLine

object HelloInteractive extends App {
     
    println("Hello world")
    print("Quel est ton prenom: ")
    val prenom = readLine()

    print("Quel est ton nom: ")
    val nom = readLine()

    println(s"Bonjour Monsieur $prenom $nom")

}
