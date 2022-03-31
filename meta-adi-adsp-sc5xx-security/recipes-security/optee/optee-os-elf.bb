SUMMARY = "OP-TEE OS as an ELF"
DESCRIPTION = "Package OP-TEE OS back into an functional ELF so that it can be used with tools that expect ELF objects"
LICENSE = "CLOSED"

DDEPENDS = "optee-os"

DEPLOY_SRC_URI = "tee.bin"

SRC_URI = " \
	file://optee-elf.ld.in \
"

inherit deploy deploy-dep

addtask deploy after do_compile before do_install

do_compile() {
	${OBJCOPY} -I binary ${WORKDIR}/tee.bin -O elf64-littleaarch64 ${B}/tee.o

	sed -e "s/LOAD_ADDRESS/${OPTEE_LOAD_ADDRESS}/" ${WORKDIR}/optee-elf.ld.in > ${WORKDIR}/optee-elf.ld

	${CC} -nostartfiles -nostdlib -static -T ${WORKDIR}/optee-elf.ld ${B}/tee.o -o ${B}/tee.elf
}

do_install() {
	install -d ${D}/firmware
	install -m 0755 ${B}/tee.elf ${D}/firmware/tee.elf
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0755 ${B}/tee.elf ${DEPLOYDIR}/tee.elf
}

FILES_${PN} = "\
	/firmware \
	/firmware/tee.elf \
"