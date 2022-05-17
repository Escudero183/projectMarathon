<template>
  <div>
    <empresa-form
      :is-toggle-sidebar.sync="isToggleSidebar"
      :form-type="formType"
      :data-edit="dataEdit"
      @refetch-data="refetchData"
      @error-data="showError"
    />

    <!-- Table Container Card -->
    <b-card
      no-body
      class="mb-0"
    >

      <div class="m-2">
        <!-- Table Top -->
        <b-row>
          <!-- Per Page -->
          <b-col
            cols="12"
            md="6"
            class="d-flex align-items-center justify-content-start mb-1 mb-md-0"
          >
            <label>Mostrando</label>
            <v-select
              v-model="perPage"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="pageOptions"
              :clearable="false"
              class="per-page-selector d-inline-block mx-50"
            />
            <label>registros</label>
          </b-col>

          <!-- Search -->
          <b-col
            cols="12"
            md="6"
          >
            <div class="d-flex align-items-center justify-content-end">
              <b-form-input
                v-model="query"
                class="d-inline-block mr-1"
                placeholder="Buscar..."
              />
              <b-button
                variant="primary"
                @click="openForm('new')"
              >
                <span class="text-nowrap">Agregar</span>
              </b-button>
            </div>
          </b-col>
        </b-row>

      </div>

      <b-table
        ref="refUserListTable"
        class="position-relative"
        :items="items"
        responsive
        :fields="fields"
        primary-key="id"
        :sort-by.sync="sortBy"
        show-empty
        empty-text="No se encontraron registros"
        :busy="isBusy"
      >
        <template #table-busy>
          <div class="text-center text-primary my-2">
            <b-spinner
              variant="primary"
              class="align-middle"
            />
            <strong class="ml-1">Cargando...</strong>
          </div>
        </template>
        <template #cell(razonSocial)="data">
          <b-media vertical-align="center">
            <template #aside>
              <b-avatar
                size="32"
                :src="`data:image/jpeg;base64,${data.item.foto}`"
                :text="avatarText(`${data.item.razonSocial}`)"
                :variant="`light-${getColorAvatar()}`"
              />
            </template>
            <span class="font-weight-bold d-block text-nowrap">
              {{ data.item.razonSocial }}
            </span>
            <small class="text-muted">{{ data.item.representanteLegal }}</small>
            <small class="text-muted d-block ">{{ data.item.tipoDocumento.abreviatura }} {{ data.item.documento }}</small>
          </b-media>
        </template>

        <template #cell(tipoProducto)="data">
          <template v-if="data.item.tipoProducto === 'CILINDRO'">
            <b-badge
              pill
              variant="light-success"
            >
              {{ data.item.tipoProducto }}
            </b-badge>
          </template>
          <template v-if="data.item.tipoProducto === 'GRANEL'">
            <b-badge
              pill
              variant="light-info"
            >
              {{ data.item.tipoProducto }}
            </b-badge>
          </template>
          <template v-if="data.item.tipoProducto === 'AMBOS'">
            <b-badge
              pill
              variant="light-secondary"
            >
              {{ data.item.tipoProducto }}
            </b-badge>
          </template>
        </template>

        <template #cell(capacidadAutorizada)="data">
          <template v-if="data.item.tipoProducto === 'CILINDRO'">
            <span class="font-weight-bold d-block text-nowrap">
              {{ data.item.capacidadAutorizada }} KG
            </span>
          </template>
          <template v-if="data.item.tipoProducto === 'GRANEL'">
            <div>
              <label class="font-weight-bold">Total {{ data.item.capacidadGranel }} GAL</label>
              <b-progress
                :max="data.item.capacidadGranel"
              >
                <b-progress-bar :value="data.item.capacidadActualPorc/100*data.item.capacidadGranel">
                  <strong>{{ data.item.capacidadActualPorc }}%</strong>
                </b-progress-bar>
              </b-progress>
            </div>
          </template>
          <template v-if="data.item.tipoProducto === 'AMBOS'">
            <span class="font-weight-bold d-block text-nowrap">
              {{ data.item.capacidadAutorizada }} KG | {{ data.item.capacidadGranel }} GAL / {{ data.item.capacidadActualPorc }}%
            </span>
          </template>
        </template>

        <template #cell(ubigeoAll)="data">
          <b-media vertical-align="center">
            <span class="font-weight-bold d-block font-small-3">
              {{ data.item.direccion }}
            </span>
            <small class="text-muted font-small-2">{{ data.item.ubigeoAll.dpto }} / {{ data.item.ubigeoAll.prov }} / {{ data.item.ubigeoAll.dist }}</small>
          </b-media>
          <div class="d-block">
            <b-link
              v-if="data.item.celular != '' && data.item.celular != null"
              v-b-tooltip.hover.top="`Celular: ${data.item.celular}`"
              :href="`tel:${data.item.celular}`"
              target="_blank"
              class="font-weight-bold"
            >
              <b-avatar
                :id="`btn-cel-${data.item.idTrabajador}`"
                size="32"
                :variant="`light-info`"
              >
                <feather-icon
                  icon="SmartphoneIcon"
                />
              </b-avatar>
            </b-link>
            <b-link
              v-if="data.item.correo != '' && data.item.correo != null"
              v-b-tooltip.hover.top="data.item.correo"
              :href="`mailto:${data.item.correo}`"
              target="_blank"
              class="font-weight-bold ml-50"
            >
              <b-avatar
                :id="`btn-correo-${data.item.idTrabajador}`"
                size="32"
                :variant="`light-primary`"
              >
                <feather-icon
                  icon="MailIcon"
                />
              </b-avatar>
            </b-link>
            <b-link
              v-if="data.item.latitud != '' && data.item.latitud != null && data.item.longitud != '' && data.item.longitud != null"
              v-b-tooltip.hover.top="'Ir a Google Maps'"
              :href="`https://maps.google.com/?ll=${data.item.latitud},${data.item.longitud}&z=14`"
              target="_blank"
              class="font-weight-bold ml-50"
            >
              <b-avatar
                :id="`btn-ubi-${data.item.idTrabajador}`"
                size="32"
                :variant="`light-warning`"
              >
                <feather-icon
                  icon="MapPinIcon"
                />
              </b-avatar>
            </b-link>
          </div>
        </template>

        <!-- Column: Actions -->
        <template #cell(acciones)="data">
          <b-dropdown
            variant="link"
            no-caret
            :right="$store.state.appConfig.isRTL"
          >

            <template #button-content>
              <feather-icon
                icon="MoreVerticalIcon"
                size="16"
                class="align-middle text-body"
              />
            </template>

            <b-dropdown-item @click="getEdit(data)">
              <feather-icon icon="EditIcon" />
              <span class="align-middle ml-50">Editar</span>
            </b-dropdown-item>

            <b-dropdown-item @click="getDelete(data)">
              <feather-icon icon="TrashIcon" />
              <span class="align-middle ml-50">Eliminar</span>
            </b-dropdown-item>
          </b-dropdown>
        </template>

      </b-table>
      <div class="mx-2 mb-2">
        <b-row>

          <b-col
            cols="12"
            sm="6"
            class="d-flex align-items-center justify-content-center justify-content-sm-start"
          >
            <span class="text-muted">Mostrando {{ from }} a {{ to }} de {{ of }} registros</span>
          </b-col>
          <!-- Pagination -->
          <b-col
            cols="12"
            sm="6"
            class="d-flex align-items-center justify-content-center justify-content-sm-end"
          >

            <b-pagination
              v-model="page"
              :total-rows="totalItems"
              :per-page="perPage"
              first-number
              last-number
              class="mb-0 mt-1 mt-sm-0"
              prev-class="prev-item"
              next-class="next-item"
            >
              <template #prev-text>
                <feather-icon
                  icon="ChevronLeftIcon"
                  size="18"
                />
              </template>
              <template #next-text>
                <feather-icon
                  icon="ChevronRightIcon"
                  size="18"
                />
              </template>
            </b-pagination>

          </b-col>

        </b-row>
      </div>
    </b-card>
  </div>
</template>

<script>
import {
  BCard, BRow, BCol, BFormInput, BButton, BTable, BBadge, BDropdown, BDropdownItem, BPagination, BSpinner, BAvatar, BMedia, VBTooltip, BLink, BProgress, BProgressBar,
} from 'bootstrap-vue'
import vSelect from 'vue-select'
import store from '@/store'
import { showToast, showError } from '@/helpers'
import { avatarText } from '@core/utils/filter'
import EmpresaForm from './EmpresaForm.vue'

export default {
  components: {
    EmpresaForm,
    BCard,
    BRow,
    BCol,
    BFormInput,
    BButton,
    BTable,
    BBadge,
    BDropdown,
    BDropdownItem,
    BPagination,

    vSelect,
    BSpinner,
    BAvatar,
    BMedia,
    BLink,
    BProgress,
    BProgressBar,
  },
  directives: {
    'b-tooltip': VBTooltip,
  },
  data: () => ({
    isBusy: false,
    idEmpresa: 3,
    isToggleSidebar: false,
    dataEdit: {},
    formType: '',
    page: 1,
    perPage: 10,
    totalRows: 0,
    totalItems: 0,
    totalPage: 0,
    from: 0,
    to: 0,
    of: 0,
    pageOptions: [10, 20, 50],
    sortBy: null,
    sortDesc: false,
    sortDirection: 'asc',
    query: '',
    items: [],
    fields: [
      { key: 'idCliente', sortable: true, thClass: 'd-none', tdClass: 'd-none' },
      { key: 'razonSocial', label: 'Cliente', sortable: true },
      { key: 'tipoProducto', label: 'Rubro', sortable: true },
      { key: 'capacidadAutorizada', label: 'Capacidad', sortable: true, tdClass: 'text-center', thClass: 'text-center' },
      { key: 'ubigeoAll', label: 'Contacto', sortable: true, tdClass: 'w200', thClass: 'w200' },
      { key: 'acciones' },
    ],
  }),
  watch: {
    perPage: {
      handler() {
        this.listarRegistros()
      },
    },
    page: {
      handler() {
        this.listarRegistros()
      },
    },
    query: {
      handler() {
        this.listarRegistros()
      },
    },
  },
  mounted() {
    this.listarRegistros()
  },
  methods: {
    async listarRegistros() {
      this.isBusy = true
      await store
        .dispatch('proceso/EMPRESA_FIND_ALL', {
          limit: this.perPage,
          query: this.query,
          page: this.page,
          sortBy: 'idEmpresa%7CDESC',
        })
        .then(response => {
          this.items = response.items
          this.totalRows = response.totalRows
          this.totalItems = response.totalItems
          this.totalPage = response.totalPage
          this.from = this.perPage * (this.page - 1) + (this.totalRows ? 1 : 0)
          this.to = this.perPage * (this.page - 1) + this.totalRows
          this.of = this.totalItems
        })
        .catch(error => {
          this.showToast('Ha ocurrido un error', error, 'danger')
        })
      this.isBusy = false
    },
    openForm(type) {
      this.formType = type
      this.isToggleSidebar = true
    },
    getEdit(data) {
      this.formType = 'edit'
      this.isToggleSidebar = true
      console.log(data.item)
      this.dataEdit = data.item
    },
    getDelete(data) {
      this.$swal({
        title: 'Está seguro?',
        text: 'Va eliminar un registro!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'No',
        customClass: {
          confirmButton: 'btn btn-danger',
          cancelButton: 'btn btn-primary ml-1',
        },
        buttonsStyling: false,
      }).then(result => {
        if (result.value) {
          store
            .dispatch('proceso/EMPRESA_DELETE', {
              id: data.item.idCliente,
            })
            .then(response => {
              this.listarRegistros()
              this.showToast('Operación Extitosa', 'Ha Eliminado correctamente el registro.', 'success')
              return response
            })
            .catch(error => {
              this.showToast('Ha ocurrido un error', error, 'danger')
            })
        }
      })
    },
    refetchData(response) {
      let message = 'El proceso se ha ejecutado satisfactoriamente.'
      if (response.message) {
        message = response.message
      }
      this.showToast('Operación Extitosa', message, 'success')
      this.listarRegistros()
    },
    getColorAvatar() {
      const max = 7
      const min = 1
      const status = Math.floor(Math.random() * ((max + 1) - min) + min)
      if (status === 1) return 'primary'
      if (status === 2) return 'danger'
      if (status === 3) return 'secondary'
      if (status === 4) return 'warning'
      if (status === 5) return 'info'
      if (status === 6) return 'success'
      return 'primary'
    },
    avatarText,
    showToast,
    showError,
  },
}
</script>

<style lang="scss" scoped>
.per-page-selector {
  width: 90px;
}
</style>

<style lang="scss">
@import '@core/scss/vue/libs/vue-select.scss';
</style>

<style>
.w200{
  width: 350px !important;
}
</style>
