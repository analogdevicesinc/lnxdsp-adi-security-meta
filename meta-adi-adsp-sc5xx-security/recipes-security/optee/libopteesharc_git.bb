# TODO: Update license
SUMMARY = "ADI SHARC control via OP-TEE"
DESCRIPTION = "Library and CLI for controlling on-chip SHARCs through OP-TEE"
LICENSE = "CLOSED"

DEPENDS = "optee-client"
SRC_URI = "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/libopteesharc.git;branch=master;protocol=ssh"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

include optee.inc

do_compile() {
	kludge_openssl
	oe_runmake
}

do_install() {
	install -d ${D}/usr/bin
	install -d ${D}/usr/lib

	install -m 0755 ${S}/libopteesharc.so ${D}/usr/lib/libopteesharc.so.1.0
	ln -sf libopteesharc.so.1.0 ${D}/usr/lib/libopteesharc.so.1
	install -m 0755 ${S}/sharc-cli ${D}/usr/bin/sharc-cli

	install -d ${D}/usr/include
	install -m 0755 ${S}/include/libopteesharc.h ${D}/usr/include/libopteesharc.h
	install -m 0755 ${S}/include/adi_sharc_pta.h ${D}/usr/include/adi_sharc_pta.h
}

FILES:${PN} = " \
	/usr/bin/sharc-cli \
	/usr/lib/libopteesharc.so.1.0 \
	/usr/lib/libopteesharc.so.1 \
"
FILES:${PN}-dev += " \
	/usr/include/libopteesharc.h \
	/usr/include/adi_sharc_pta.h \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
