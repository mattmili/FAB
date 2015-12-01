# Reviewr

Authors: Matthew Militante & Alexander Gladu

## Description
Reviewr is an easy-to-use Android application that displays reviews of your favorite books!    

## How we built it
Android, Flask, Heroku, Microsoft Oxford Image Recognition API, iDreamBooks API

## Special Features

A feature we wanted to include was the ability to take a photograph of a book, and automatically display reviews for that book. In order to acheive this, we decided to use Microsoft's Oxford Vision API. This API uses Optimal Character Recognition to detect text in an image and extract the recognized characters into a machine-usable character stream. This API is still in BETA and is currently not as accurate as we thought, however, moving forward this would eventually replace the manual entry of book titles and ISBN numbers.

## Project Requirements completed

Multiple Activities:
  - BookEntry, LoadingScreen, MainActivity, ParseDisplay, Result

Media Playback: 
  - An "book-flipping" mp3 file is played upon starting the loading screen acivity. 

Downloading Internet Resources: 
  - Microsoft Oxford Vision API:
    - Image analyzer that analyzes an image for text

  - Retrieving Book reviews:
    - A server which sends a request to the iDreamBooks API was created to server as a middle-man for the Android application. By implementing a server that calls the API, this removes having to embed the API call into the application. Furthermore, this will help scalability in the future. 
    - This was built using Flask (server address: https://damp-wildwood-1388.herokuapp.com/)

  - Retrieving ISBN and image of Book cover:
    - Calls are directly made to the iDreambooks API in order to retrieve the ISBN
    - To get the cover, a URL for another site (OpenLibrary) is created with the isbns in the list of isbns the previous method created. This cycles through the list until a book cover is found. It will then, depending on how long the process took, display either the book cover, or a default icon on the results_activity page.

Storage Access/Database:
  - A database was used to store previous searches
  - Photos analyzed by the Microsoft Oxford API are stored temporarily on the device








