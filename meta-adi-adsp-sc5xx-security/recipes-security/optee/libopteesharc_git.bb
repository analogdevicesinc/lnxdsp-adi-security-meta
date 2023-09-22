SUMMARY = "ADI SHARC control via OP-TEE"
DESCRIPTION = "Library and CLI for controlling on-chip SHARCs through OP-TEE"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=83a4fa88496792b47ea6cfdc3456dcaf"

DEPENDS = "optee-client"
SRC_URI = "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/libopteesharc.git;branch=master;protocol=ssh"
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
	install -m 0755 ${B}/libopteesharc.so ${D}${libdir}/libopteesharc.so.1.0
	ln -sf libopteesharc.so.1.0 ${D}${libdir}/libopteesharc.so.1

	install -d ${D}${bindir}
	install -m 0755 ${B}/sharc-cli ${D}${bindir}/

	install -d ${D}${includedir}
	install -m 0755 ${B}/include/libopteesharc.h ${D}${includedir}/libopteesharc.h
	install -m 0755 ${B}/include/adi_sharc_pta.h ${D}${includedir}/adi_sharc_pta.h
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
