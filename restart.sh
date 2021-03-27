#!/bin/bash

echo " _       __________    __________  __  _________   ___    _   ______  ____  _______       __"
echo "| |     / / ____/ /   / ____/ __ \/  |/  / ____/  /   |  / | / / __ \/ __ \/ ____/ |     / /"
echo "| | /| / / __/ / /   / /   / / / / /|_/ / __/    / /| | /  |/ / / / / /_/ / __/  | | /| / / "
echo "| |/ |/ / /___/ /___/ /___/ /_/ / /  / / /___   / ___ |/ /|  / /_/ / _, _/ /___  | |/ |/ /  "
echo "|__/|__/_____/_____/\____/\____/_/  /_/_____/  /_/  |_/_/ |_/_____/_/ |_/_____/  |__/|__/   "

cd /home/ec2-user/andrew/Personal

echo "########################################"
echo "############## Git Pull ###############"
echo "########################################"
echo ""
git pull
echo ""
echo "########################################"
echo "########### Git Pull Complete ##########"
echo "########################################"
echo ""
echo ""
echo ""
echo "########################################"
echo "########## Start Build Gradle ##########"
echo "########################################"
echo ""
./gradlew build
echo ""
echo "########################################"
echo "######## Complete Build Gradle #########"
echo "########################################"
echo ""
echo ""
echo ""
echo "########################################"
echo "########## Check Current PID ###########"
echo "########################################"
echo ""
CURRENT_PID=$(pgrep -f personal)
echo ""
echo "########################################"
echo "##### Current PID : $CURRENT_PID #######"
echo "########################################"
echo ""
echo ""
echo ""
if [ -z "$CURRENT_PID" ]; then
   echo ">No Server Is Currently Running"
else
   echo "> kill -15 $CURRENT_PID"
   kill -15 $CURRENT_PID
   sleep 5
fi
echo ""
echo "  __ \                |               "
echo "  |   |   _ \  __ \   |   _ \   |   | "
echo "  |   |   __/  |   |  |  (   |  |   | "
echo " ____/  \___|  .__/  _| \___/  \__, | "
echo "              _|               ____/  "
echo ""
echo ""
rm nohup.out

nohup java -jar /home/ec2-user/andrew/Personal/build/libs/personal-0.0.1-SNAPSHOT.jar &

echo ""
echo ""
echo "   ___|   _ \    \  |   _ \   |      ____| __ __|  ____| "
echo "  |      |   |  |\/ |  |   |  |      __|      |    __|   "
echo "  |      |   |  |   |  ___/   |      |        |    |     "
echo " \____| \___/  _|  _| _|     _____| _____|   _|   _____| "
echo "                                                         "

tail -1000 nohup.out