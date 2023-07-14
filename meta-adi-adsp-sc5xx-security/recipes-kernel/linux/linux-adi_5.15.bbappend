FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:adsp-sc5xx-optee = " \
	file://0001-enable-optee-configuration.patch \
	file://0001-sc598-disable-dmc-pmu.patch \
"
