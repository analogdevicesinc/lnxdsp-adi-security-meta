SUMMARY = "OP-TEE Client API"
DESCRIPTION = "Open Portable Trusted Execution Environment - Normal World Client side of the TEE"
HOMEPAGE = "https://www.op-tee.org/"

LICENSE = "BSD-2-Clause"

inherit systemd cmake

SRC_URI = " \
    git://github.com/OP-TEE/optee_client.git;branch=master;protocol=https \
    file://tee-supplicant.service \
"
inherit pkgconfig
DEPENDS += "util-linux"
EXTRA_OEMAKE += "PKG_CONFIG=pkg-config"

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " \
    -DBUILD_SHARED_LIBS=ON \
"

do_compile:append() {
    sed -e's,@sbindir@,${sbindir},g' \
        -e's,@sysconfdir@,${sysconfdir},g' \
        ${WORKDIR}/tee-supplicant.service >${B}/tee-supplicant.service
}

do_install:append() {
    install -D -p -m0644 ${B}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
}

SYSTEMD_SERVICE:${PN} = "tee-supplicant.service"
