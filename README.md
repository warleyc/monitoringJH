README for monitoring
==========================



Heroku

Your app should now be live. To view it run
	heroku open
And you can view the logs with this command
	heroku logs --tail
After application modification, repackage it with
	mvn package -Pprod -DskipTests
And then re-deploy it with
	heroku deploy:jar --jar target/*.war
