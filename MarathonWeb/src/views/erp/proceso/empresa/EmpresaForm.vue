<template>
  <b-sidebar
    id="add-new-user-sidebar"
    :visible="isToggleSidebar"
    bg-variant="white"
    sidebar-class="sidebar-lg"
    shadow
    backdrop
    no-header
    right
    @hidden="resetForm"
    @change="(val) => toggleSidebar(val)"
  >
    <template #default="{ hide }">
      <!-- Header -->
      <div class="d-flex justify-content-between align-items-center content-sidebar-header px-2 py-1">
        <h5 class="mb-0">
          {{ titleForm }}
        </h5>

        <feather-icon
          class="ml-1 cursor-pointer"
          icon="XIcon"
          size="16"
          @click="hide"
        />

      </div>

      <!-- Form -->
      <b-form
        class="p-2"
        @reset.prevent="resetForm"
      >
        <h6
          class="text-info"
        >
          <feather-icon icon="AlertCircleIcon" />
          <span class="align-middle ml-25">Datos Generales</span>
        </h6>

        <hr style="border-top: 2px solid #00cfe8;margin-bottom: 1rem !important;margin-top: 0 !important;">

        <b-form-group
          label="Buscar Empresa"
          label-for="nroDoc"
        >
          <b-input-group class="input-group-merge">
            <v-select
              v-model="tipoPersonaSel"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="tiposPersona"
              @input="tipoPersonaSel == 'NATURAL' ? isJuridica = false : isJuridica = true"
            />
            <b-form-input
              id="search-nro-doc"
              v-model="formData.documento"
              style="padding: 0.438rem 1rem;"
              :placeholder="placeholderTP"
              type="number"
            />
            <b-button
              variant="info"
              @click="searchData"
            >
              <feather-icon icon="SearchIcon" />
            </b-button>
          </b-input-group>
        </b-form-group>

        <div
          v-if="isBusy"
          class="text-center text-primary my-2"
        >
          <b-spinner
            variant="primary"
            class="align-middle"
          />
          <strong class="ml-1">Cargando...</strong>
        </div>

        <b-media
          v-show="!isJuridica"
        >
          <template #aside>
            <b-avatar
              ref="previewEl"
              :src="`data:image/jpeg;base64,${fotoReniec}`"
              size="132px"
              rounded
              style="width: 85px; height: 134px;"
            />
          </template>

          <b-row>
            <b-col
              cols="12"
              md="12"
            >
              <b-form-group
                label="Razón Social"
                label-for="razonSocial2"
              >
                <b-form-input
                  id="razonSocial2"
                  v-model="formData.razonSocial"
                />
              </b-form-group>
            </b-col>
          </b-row>
          <b-row>
            <b-col
              cols="12"
              md="12"
            >
              <b-form-group
                label="Representante Legal"
                label-for="representanteLegal2"
              >
                <b-form-input
                  id="representanteLegal2"
                  v-model="formData.representanteLegal"
                />
              </b-form-group>
            </b-col>
          </b-row>
        </b-media>

        <b-row>
          <b-col
            v-show="isJuridica"
            cols="12"
            md="12"
          >
            <b-form-group
              label="Razón Social"
              label-for="razonSocial"
            >
              <b-form-input
                id="razonSocial"
                v-model="formData.razonSocial"
              />
            </b-form-group>
          </b-col>
          <b-col
            v-show="isJuridica"
            cols="12"
            md="12"
          >
            <b-form-group
              label="Representante Legal"
              label-for="representanteLegal"
            >
              <b-form-input
                id="representanteLegal"
                v-model="formData.representanteLegal"
              />
            </b-form-group>
          </b-col>
        </b-row>

        <h6
          class="text-info"
        >
          <feather-icon icon="CheckCircleIcon" />
          <span class="align-middle ml-25">Datos del Rubro</span>
        </h6>

        <hr style="border-top: 2px solid #00cfe8;margin-bottom: 1rem !important;margin-top: 0 !important;">

        <b-row>
          <b-col
            cols="12"
            md="6"
          >
            <b-form-group
              label="Tipo Producto"
              label-for="tipoProducto"
            >
              <v-select
                v-model="tipoProductoSel"
                :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
                :options="tiposProducto"
                @input="showBox()"
              />
            </b-form-group>
          </b-col>
          <b-col
            cols="12"
            md="6"
          >
            <b-form-group
              label="Código Osinergmin"
              label-for="codigoOsinergmin"
            >
              <b-form-input
                id="codigoOsinergmin"
                v-model="formData.codigoOsinergmin"
              />
            </b-form-group>
          </b-col>
          <b-col
            cols="12"
            md="12"
          >
            <b-form-group
              v-if="isCilindro"
              label="Capacidad Autorizada"
              label-for="capacidadAutorizada"
            >
              <b-input-group
                append="KG"
              >
                <b-form-input
                  id="capacidadAutorizada"
                  v-model="formData.capacidadAutorizada"
                  type="number"
                />
              </b-input-group>
            </b-form-group>

            <b-form-group
              v-if="isGranel"
              label="Capacidad Granel"
              label-for="capacidadGranel"
            >
              <b-input-group
                append="GAL"
              >
                <b-form-input
                  id="capacidadGranel"
                  v-model="formData.capacidadGranel"
                  type="number"
                />
              </b-input-group>
            </b-form-group>

            <b-form-group
              v-if="isGranel"
              label="Capacidad Actual"
              label-for="capacidadActualPorc"
            >
              <b-input-group
                append="%"
              >
                <b-form-input
                  id="capacidadActualPorc"
                  v-model="formData.capacidadActualPorc"
                  type="number"
                />
              </b-input-group>
            </b-form-group>
          </b-col>
        </b-row>

        <h6
          class="text-info"
        >
          <feather-icon icon="MapPinIcon" />
          <span class="align-middle ml-25">Ubicación</span>
        </h6>

        <hr style="border-top: 2px solid #00cfe8;margin-bottom: 1rem !important;margin-top: 0 !important;">

        <b-form-group
          label="Seleccione Departamento"
          label-for="dpto"
        >
          <v-select
            v-model="dptoSel"
            :reduce="m => m.idUbigeo"
            label="descripcion"
            :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
            :options="dptos"
            @input="getUbigeos('prov', dptoSel.slice(0, 2))"
          />
        </b-form-group>
        <b-form-group
          label="Seleccione Provincia"
          label-for="prov"
        >
          <v-select
            v-model="provSel"
            :reduce="m => m.idUbigeo"
            label="descripcion"
            :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
            :options="provs"
            @input="getUbigeos('dist', provSel.slice(0, 4))"
          />
        </b-form-group>
        <b-form-group
          label="Seleccione Distrito"
          label-for="dist"
        >
          <v-select
            v-model="distSel"
            :reduce="m => m.idUbigeo"
            label="descripcion"
            :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
            :options="dists"
          />
        </b-form-group>

        <b-form-group
          label="Dirección"
          label-for="direccion"
        >
          <b-form-input
            id="direccion"
            v-model="formData.direccion"
          />
        </b-form-group>

        <b-form-group
          label="Latitud"
          label-for="latitud"
        >
          <b-form-input
            id="latitud"
            v-model="formData.latitud"
            trim
            placeholder=""
          />
        </b-form-group>

        <b-form-group
          label="Longitud"
          label-for="longitud"
        >
          <b-form-input
            id="longitud"
            v-model="formData.longitud"
            trim
            placeholder=""
          />
        </b-form-group>

        <h6
          class="text-info"
        >
          <feather-icon icon="PhoneCallIcon" />
          <span class="align-middle ml-25">Datos de Contacto</span>
        </h6>

        <hr style="border-top: 2px solid #00cfe8;margin-bottom: 1rem !important;margin-top: 0 !important;">

        <b-row>
          <b-col
            cols="12"
            md="12"
          >
            <b-form-group
              label="Celular"
              label-for="celular"
            >
              <b-form-input
                id="celular"
                v-model="formData.celular"
                type="number"
              />
            </b-form-group>
          </b-col>
          <b-col
            cols="12"
            md="12"
          >
            <b-form-group
              label="Correo"
              label-for="correo"
            >
              <b-form-input
                id="correo"
                v-model="formData.correo"
                type="email"
              />
            </b-form-group>
          </b-col>
        </b-row>

        <!-- Form Actions -->
        <div class="d-flex mt-2">
          <b-button
            v-ripple.400="'rgba(255, 255, 255, 0.15)'"
            variant="primary"
            class="mr-2"
            type="button"
            @click="saveForm"
          >
            Guardar
          </b-button>
          <b-button
            v-ripple.400="'rgba(186, 191, 199, 0.15)'"
            type="button"
            variant="outline-secondary"
            @click="hide"
          >
            Cancelar
          </b-button>
        </div>

      </b-form>
    </template>
  </b-sidebar>
</template>

<script>
import {
  BRow, BCol, BAvatar, BMedia, BSidebar, BForm, BFormGroup, BFormInput, BInputGroup, BButton, BSpinner,
} from 'bootstrap-vue'
import vSelect from 'vue-select'
import { ref } from '@vue/composition-api'
import { required } from '@validations'
// import moment from 'moment'
import Ripple from 'vue-ripple-directive'
import store from '@/store'

export default {
  components: {
    BRow,
    BCol,
    BAvatar,
    BMedia,
    BSidebar,
    BForm,
    BFormGroup,
    BFormInput,
    BInputGroup,
    BButton,
    vSelect,
    BSpinner,
  },
  directives: {
    Ripple,
  },
  model: {
    prop: 'isToggleSidebar',
    event: 'update:is-toggle-sidebar',
  },
  props: {
    isToggleSidebar: {
      type: Boolean,
      required: true,
    },
    dataEdit: {
      type: Object,
      required: false,
      default: null,
    },
    formType: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      required,
    }
  },
  setup(props, { emit }) {
    const titleForm = ref('')
    const isBusy = ref(false)
    const tiposDocument = ref([])
    const tipoDocSel = ref(null)
    const formData = ref({})
    const fotoReniec = ref('')
    const cargos = ref(['GERENTE GENERAL', 'GERENTE OPERACIONES', 'GERENTE RRHH', 'SUPERVISOR COMERCIAL ZONAL', 'SUPERVISOR GRANEL', 'COORDINADOR OSINERGMIN', 'CAJERA', 'CONTADOR', 'JEFE DEPOSITO GASOCENTRO', 'JEFE PLATAFORMA', 'CONDUCTOR TRANSPORTE PRIMARIO', 'CONDUCTOR TRANSPORTE SECUNDARIO', 'VENDEDOR GRANEL', 'VENDEDOR CILINDROS', 'VENDEDOR DEPOSITO', 'VENDEDOR GASOCENTRO', 'VIGILANTE', 'DELIVERISTA'])
    const cargoSel = ref(null)
    const sexos = ref(['MASCULINO', 'FEMENINO'])
    const sexoSel = ref('MASCULINO')
    const readonlyFrm = ref(false)
    const tiposPersona = ref(['JURIDICA', 'NATURAL'])
    const tipoPersonaSel = ref('JURIDICA')
    const tiposProducto = ref(['CILINDRO', 'GRANEL', 'AMBOS'])
    const tipoProductoSel = ref('CILINDRO')
    const isJuridica = ref(true)
    const dptoSel = ref('220000')
    const provSel = ref(null)
    const distSel = ref(null)
    const dptos = ref([])
    const provs = ref([])
    const dists = ref([])
    const placeholderTP = ref('Ingrese RUC')
    const isGranel = ref(false)
    const isCilindro = ref(false)

    const resetForm = () => {
      formData.value = {}
      fotoReniec.value = ''
    }

    const getTiposDocumento = async () => {
      await store.dispatch('catalogo/TIPO_DOCUMENTO_FIND_ALL')
        .then(response => {
          if (response) {
            tiposDocument.value = response
            tipoDocSel.value = tiposDocument.value.find(id => id.idTipoDocumento === 1)
          }
        })
    }

    const showBox = () => {
      if (tipoProductoSel.value === 'GRANEL') {
        isGranel.value = true
        isCilindro.value = false
      }

      if (tipoProductoSel.value === 'CILINDRO') {
        isGranel.value = false
        isCilindro.value = true
      }

      if (tipoProductoSel.value === 'AMBOS') {
        isGranel.value = true
        isCilindro.value = true
      }
    }

    const searchData = async () => {
      isBusy.value = true

      if (tipoPersonaSel.value === 'JURIDICA') {
        placeholderTP.value = 'Ingrese RUC'
        await store
          .dispatch('helpers/GET_DATOS_BY_RUC', {
            dni: formData.value.documento,
          })
          .then(response => {
            isBusy.value = false
            formData.value.representanteLegal = response.nombre
            formData.value.razonSocial = response.nombre
            formData.value.foto = ''
            fotoReniec.value = ''
          })
          .catch(error => {
            isBusy.value = false
            emit('error-data', error)
          })
      }

      if (tipoPersonaSel.value === 'NATURAL') {
        placeholderTP.value = 'Ingrese DNI'
        await store
          .dispatch('helpers/RENIEC_FIND_CIUDADANO', {
            dni: formData.value.documento,
          })
          .then(response => {
            if (response.datosPersona) {
              formData.value.representanteLegal = `${response.datosPersona.prenombres} ${response.datosPersona.apPrimer} ${response.datosPersona.apSegundo}`
              formData.value.razonSocial = `${response.datosPersona.prenombres} ${response.datosPersona.apPrimer} ${response.datosPersona.apSegundo}`
              formData.value.foto = response.datosPersona.foto
              formData.value.direccion = response.datosPersona.direccion
              fotoReniec.value = response.datosPersona.foto
            }
            isBusy.value = false
          })
          .catch(error => {
            isBusy.value = false
            emit('error-data', error)
          })
      }
    }

    const getRuc = async () => {
      isBusy.value = true
      await store
        .dispatch('helpers/GET_DATOS_BY_RUC', {
          dni: formData.value.documento,
        })
        .then(response => {
          isBusy.value = false
          console.log(response)
        })
        .catch(error => {
          isBusy.value = false
          emit('error-data', error)
        })
    }

    const saveForm = async () => {
      // Validar datos antes
      if (tipoPersonaSel.value === null) {
        emit('error-data', { response: { data: { message: 'Debe seleccionar el Tipo de Persona' } } })
        return
      }

      if (formData.value.documento === null || formData.value.documento === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar número de documento del Cliente' } } })
        return
      }

      if (formData.value.razonSocial === null || formData.value.razonSocial === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar Razón Social del Cliente' } } })
        return
      }

      if (dptoSel.value === null) {
        emit('error-data', { response: { data: { message: 'Debe seleccionar departamento donde reside el Cliente' } } })
        return
      }

      if (provSel.value === null) {
        emit('error-data', { response: { data: { message: 'Debe seleccionar provincia donde reside el Cliente' } } })
        return
      }

      if (distSel.value === null) {
        emit('error-data', { response: { data: { message: 'Debe seleccionar distrito donde reside el Cliente' } } })
        return
      }

      if (tipoProductoSel.value === null) {
        emit('error-data', { response: { data: { message: 'Debe seleccionar el Tipo de Producto' } } })
        return
      }

      let service = 'comercial/CLIENTE_CREATE'
      if (props.formType === 'edit') {
        service = 'comercial/CLIENTE_UPDATE'
      }

      if (tipoPersonaSel.value === 'JURIDICA') {
        formData.value.tipoDocumento = {
          idTipoDocumento: 3,
        }
        formData.value.foto = ''
      }

      if (tipoPersonaSel.value === 'NATURAL') {
        formData.value.tipoDocumento = {
          idTipoDocumento: 1,
        }
        if (fotoReniec.value !== '') {
          formData.value.foto = fotoReniec.value
        }
      }

      // formData.value.tipoDocumento = tipoDocSel.value
      formData.value.tipoPersona = tipoPersonaSel.value
      formData.value.tipoProducto = tipoProductoSel.value
      formData.value.ubigeo = {
        idUbigeo: distSel.value,
      }

      await store.dispatch(service, formData.value)
        .then(response => {
          emit('refetch-data', response)
          emit('update:is-toggle-sidebar', false)
        })
        .catch(error => {
          emit('error-data', error)
          emit('update:is-toggle-sidebar', false)
        })
    }

    const isReadOnly = () => {
      if (tipoDocSel.value && tipoDocSel.value.id === 1) return true
      return false
    }

    const getUbigeos = async (type, codUbigeo) => {
      await store
        .dispatch('catalogo/UBIGEO_FIND_ALL', {
          get: type,
          codUbigeo,
        })
        .then(response => {
          if (type === 'dpto') {
            dptos.value = response
            provSel.value = null
            distSel.value = null
            provs.value = []
            dists.value = []
          }
          if (type === 'prov') {
            provs.value = response
            provSel.value = null
            distSel.value = null
            dists.value = []
          }
          if (type === 'dist') {
            dists.value = response
          }
        })
        .catch(error => {
          emit('error-data', error)
        })
    }

    const toggleSidebar = async val => {
      emit('update:is-toggle-sidebar', val)
      resetForm()
      await getTiposDocumento()
      if (props.formType === 'new') {
        titleForm.value = 'Agregar Cliente'
        await getUbigeos('dpto', '')
        await getUbigeos('prov', dptoSel.value.slice(0, 2))
      }
      if (props.formType === 'edit') {
        formData.value = {
          idCliente: props.dataEdit.idCliente,
          tipoPersona: props.dataEdit.tipoPersona,
          documento: props.dataEdit.documento,
          razonSocial: props.dataEdit.razonSocial,
          representanteLegal: props.dataEdit.representanteLegal,
          direccion: props.dataEdit.direccion,
          latitud: props.dataEdit.latitud,
          longitud: props.dataEdit.longitud,
          foto: props.dataEdit.foto,
          codigoOsinergmin: props.dataEdit.codigoOsinergmin,
          capacidadAutorizada: props.dataEdit.capacidadAutorizada,
          capacidadGranel: props.dataEdit.capacidadGranel,
          capacidadActualPorc: props.dataEdit.capacidadActualPorc,
          celular: props.dataEdit.celular,
          correo: props.dataEdit.correo,
          estado: props.dataEdit.estado,
        }
        tipoProductoSel.value = props.dataEdit.tipoProducto
        tipoPersonaSel.value = props.dataEdit.tipoPersona
        tipoDocSel.value = props.dataEdit.tipoDocumento
        fotoReniec.value = props.dataEdit.foto
        dptoSel.value = `${props.dataEdit.ubigeo.idUbigeo.slice(0, 2)}0000`
        await getUbigeos('dpto', '')
        await getUbigeos('prov', props.dataEdit.ubigeo.idUbigeo.slice(0, 2))
        provSel.value = `${props.dataEdit.ubigeo.idUbigeo.slice(0, 4)}00`
        await getUbigeos('dist', props.dataEdit.ubigeo.idUbigeo.slice(0, 4))
        distSel.value = props.dataEdit.ubigeo.idUbigeo
        if (tipoPersonaSel.value === 'NATURAL') {
          isJuridica.value = false
        } else {
          isJuridica.value = true
        }
        titleForm.value = 'Editar Cliente'
      }
      showBox()
    }

    getTiposDocumento()

    return {
      titleForm,
      isBusy,
      tiposDocument,
      tipoDocSel,
      formData,
      getRuc,
      searchData,
      fotoReniec,
      saveForm,
      toggleSidebar,
      isReadOnly,
      cargos,
      cargoSel,
      sexos,
      sexoSel,
      resetForm,
      readonlyFrm,
      tiposPersona,
      tipoPersonaSel,
      isJuridica,
      dptoSel,
      provSel,
      distSel,
      dptos,
      provs,
      dists,
      getUbigeos,
      placeholderTP,
      tiposProducto,
      tipoProductoSel,
      isGranel,
      isCilindro,
      showBox,
    }
  },
}
</script>

<style lang="scss">
@import '@core/scss/vue/libs/vue-select.scss';

#add-new-user-sidebar {
  .vs__dropdown-menu {
    max-height: 200px !important;
  }
}
</style>
