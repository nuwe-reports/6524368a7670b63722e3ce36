# ACCWE Hospital API
### Description:
This microservice allows to the hospital manage their doctors, patients and rooms. Allowing them an easy way to create and manage appointments without overlaps.


![Alt text](UML_Diagram.jpg?raw=true "API Endpoints flow")

It's structured into two essential layers: the controller layer and the repository layer. 
1. Controller Layer:
It serves as the entry point for incoming requests. It is responsible for handling these requests and managing the flow of data within the microservice.
2. Repository layer:
The repository layer is tasked to communicate with the database to store or retrieve the information.