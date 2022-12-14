# TODO: Update license
SUMMARY = "ADI OTP Interface Library"
DESCRIPTION = "Library and CLI for interacting with OTP memory via OP-TEE"
LICENSE = "CLOSED"

DEPENDS = "optee-client"

SRC_URI = "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/libadiotp.git;branch=master;protocol=ssh"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

include optee.inc

do_compile() {
	oe_runmake
}

do_install() {
	install -d ${D}/usr/bin
	install -d ${D}/usr/lib

	install -m 0755 ${S}/libadiotp.so ${D}/usr/lib/libadiotp.so.2.0
	ln -s libadiotp.so.2.0 ${D}/usr/lib/libadiotp.so
	install -m 0755 ${S}/adiotp-cli ${D}/usr/bin/adiotp-cli

	install -d ${D}/usr/include
	install -m 0755 ${S}/include/libadiotp.h ${D}/usr/include/libadiotp.h
	install -m 0755 ${S}/include/adi_otp_pta.h ${D}/usr/include/adi_otp_pta.h
}

FILES_${PN} = " \
	/usr/bin/adiotp-cli \
	/usr/lib/libadiotp.so.2.0 \
	/usr/lib/libadiotp.so \
"
FILES_${PN}-dev += " \
	/usr/include/libadiotp.h \
	/usr/include/adi_otp_pta.h \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
