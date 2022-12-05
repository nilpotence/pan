exec java \
	${JAVA_OPTS} \
	-noverify \
	-XX:+AlwaysPreTouch \
	-Dspring.profiles.active=prod \
	-Djava.security.egd=file:/dev/./urandom \
	-cp /app/resources/:/app/classes/:/app/libs/* \
	"fr.gouv.justice.infoparquet.InfoparquetApplication"  "$@"

