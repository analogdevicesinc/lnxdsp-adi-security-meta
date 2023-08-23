# TODO: Update license
SUMMARY = "ADI OTP Interface Library"
DESCRIPTION = "Library and CLI for interacting with OTP memory via OP-TEE"
LICENSE = "CLOSED"

DEPENDS = "optee-client"

SRC_URI = "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/libadiotp.git;branch=master;protocol=ssh"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
B = "${S}"

include optee.inc

do_compile() {
	kludge_openssl
	oe_runmake
}

do_install() {
	install -d ${D}${libdir}
	install -m 0755 ${B}/libadiotp.so ${D}${libdir}/libadiotp.so.2.0
	ln -sf libadiotp.so.2.0 ${D}${libdir}/libadiotp.so.2

	install -d ${D}${bindir}
	install -m 0755 ${B}/adiotp-cli ${D}${bindir}/

	install -d ${D}${includedir}
	install -m 0755 ${B}/include/libadiotp.h ${D}${includedir}/
	install -m 0755 ${B}/include/adi_otp_pta.h ${D}${includedir}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
