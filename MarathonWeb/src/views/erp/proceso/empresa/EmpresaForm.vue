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
          <feather-icon icon="MapPinIcon" />
          <span class="align-middle ml-25">Ubicación</span>
        </h6>

        <hr style="border-top: 2px solid #00cfe8;margin-bottom: 1rem !important;margin-top: 0 !important;">

        <b-form-group
          label="Ubigeo"
          label-for="ubigeo"
        >
          <b-form-input
            id="ubigeo"
            v-model="formData.ubigeo"
          />
        </b-form-group>

        <b-form-group
          label="Departamento"
          label-for="departamento"
        >
          <b-form-input
            id="departamento"
            v-model="formData.departamento"
          />
        </b-form-group>

        <b-form-group
          label="Provincia"
          label-for="provincia"
        >
          <b-form-input
            id="provincia"
            v-model="formData.provincia"
          />
        </b-form-group>

        <b-form-group
          label="Distrito"
          label-for="distrito"
        >
          <b-form-input
            id="distrito"
            v-model="formData.distrito"
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

        <h6
          class="text-info"
        >
          <feather-icon icon="CheckCircleIcon" />
          <span class="align-middle ml-25">Sunat</span>
        </h6>

        <hr style="border-top: 2px solid #00cfe8;margin-bottom: 1rem !important;margin-top: 0 !important;">

        <b-row>
          <b-col
            cols="12"
            md="12"
          >
            <b-form-group
              label="Estado"
              label-for="estadoSunat"
            >
              <b-form-input
                id="estadoSunat"
                v-model="formData.estadoSunat"
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
    const readonlyFrm = ref(false)
    const tiposPersona = ref(['JURIDICA', 'NATURAL'])
    const tipoPersonaSel = ref('JURIDICA')
    const isJuridica = ref(true)
    const placeholderTP = ref('Ingrese RUC')
    const resetForm = () => {
      formData.value = {}
      fotoReniec.value = ''
    }

    const searchData = async () => {
      // VALIDACIONES
      if (tipoPersonaSel.value === 'NATURAL' && formData.value.documento.length !== 8) {
        emit('error-data', { response: { data: { message: 'Debe ingresar un número de documento válido de la Empresa (DNI, 8 dígitos)' } } })
        return
      }

      if (tipoPersonaSel.value === 'JURIDICA' && formData.value.documento.length !== 11) {
        emit('error-data', { response: { data: { message: 'Debe ingresar un número de documento válido de la Empresa (RUC, 11 dígitos)' } } })
        return
      }
      isBusy.value = true

      if (tipoPersonaSel.value === 'JURIDICA') {
        placeholderTP.value = 'Ingrese RUC'
        await store
          .dispatch('helpers/GET_DATOS_BY_RUC2', {
            ruc: formData.value.documento,
            tipo: 2,
            token: '',
          })
          .then(response => {
            isBusy.value = false
            formData.value.representanteLegal = response.razon_social
            formData.value.razonSocial = response.razon_social
            formData.value.foto = ''
            fotoReniec.value = ''
            formData.value.ubigeo = response.ubigeo
            formData.value.departamento = response.departamento
            formData.value.provincia = response.provincia
            formData.value.distrito = response.distrito
            formData.value.direccion = response.direccion
            formData.value.estadoSunat = response.estado
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
              if (response.datosPersona.ubigeo.length > 0) {
                const ubigeoParams = response.datosPersona.ubigeo.split('/')
                if (ubigeoParams.length === 3) {
                  [formData.value.departamento, formData.value.provincia, formData.value.distrito] = ubigeoParams
                }
              }
              formData.value.ubigeo = ''
              formData.value.estadoSunat = ''
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

    const getRuc2 = async () => {
      isBusy.value = true
      await store
        .dispatch('helpers/GET_DATOS_BY_RUC2', {
          ruc: formData.value.documento,
          tipo: 2,
          token: '',
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
        emit('error-data', { response: { data: { message: 'Debe ingresar número de documento de la Empresa' } } })
        return
      }

      if (tipoPersonaSel.value === 'NATURAL' && formData.value.documento.length !== 8) {
        emit('error-data', { response: { data: { message: 'Debe ingresar un número de documento válido de la Empresa (DNI, 8 dígitos)' } } })
        return
      }

      if (tipoPersonaSel.value === 'JURIDICA' && formData.value.documento.length !== 11) {
        emit('error-data', { response: { data: { message: 'Debe ingresar un número de documento válido de la Empresa (RUC, 11 dígitos)' } } })
        return
      }

      if (formData.value.razonSocial === null || formData.value.razonSocial === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar Razón Social de la Empresa' } } })
        return
      }

      if (formData.value.ubigeo === null || formData.value.ubigeo === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar el código de ubigeo donde reside la Empresa' } } })
        return
      }

      if (formData.value.departamento === null || formData.value.departamento === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar departamento donde reside la Empresa' } } })
        return
      }

      if (formData.value.provincia === null || formData.value.provincia === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar provincia donde reside la Empresa' } } })
        return
      }

      if (formData.value.distrito === null || formData.value.distrito === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar distrito donde reside la Empresa' } } })
        return
      }

      if (formData.value.direccion === null || formData.value.direccion === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar dirección donde reside la Empresa' } } })
        return
      }

      if (formData.value.estadoSunat === null || formData.value.estadoSunat === '') {
        emit('error-data', { response: { data: { message: 'Debe ingresar estado sunat de la Empresa' } } })
        return
      }
      console.log('la dire >>>', formData.value.direccion)
      console.log('la razonSocial >>>', formData.value.razonSocial)

      formData.value.ruc = formData.value.documento

      let service = 'proceso/EMPRESA_CREATE'
      if (props.formType === 'edit') {
        service = 'proceso/EMPRESA_UPDATE'
      }

      await store.dispatch(service, formData.value)
        .then(response => {
          emit('refetch-data', response)
          emit('update:is-toggle-sidebar', false)
        })
        .catch(error => {
          emit('error-data', error)
          // emit('update:is-toggle-sidebar', false)
        })
    }

    const isReadOnly = () => {
      if (tipoDocSel.value && tipoDocSel.value.id === 1) return true
      return false
    }

    const toggleSidebar = async val => {
      emit('update:is-toggle-sidebar', val)
      resetForm()
      if (props.formType === 'new') {
        titleForm.value = 'Agregar Empresa'
      }
      if (props.formType === 'edit') {
        formData.value = {
          idEmpresa: props.dataEdit.idEmpresa,
          documento: props.dataEdit.ruc,
          razonSocial: props.dataEdit.razonSocial,
          representanteLegal: props.dataEdit.razonSocial,
          direccion: props.dataEdit.direccion,
          ubigeo: props.dataEdit.ubigeo,
          departamento: props.dataEdit.departamento,
          provincia: props.dataEdit.provincia,
          distrito: props.dataEdit.distrito,
          estadoSunat: props.dataEdit.estadoSunat,
          foto: props.dataEdit.foto,
          estado: props.dataEdit.estado,
        }
        fotoReniec.value = props.dataEdit.foto
        titleForm.value = 'Editar Empresa'
      }
    }

    return {
      titleForm,
      isBusy,
      tiposDocument,
      tipoDocSel,
      formData,
      getRuc,
      getRuc2,
      searchData,
      fotoReniec,
      saveForm,
      toggleSidebar,
      isReadOnly,
      resetForm,
      readonlyFrm,
      tiposPersona,
      tipoPersonaSel,
      isJuridica,
      placeholderTP,
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
