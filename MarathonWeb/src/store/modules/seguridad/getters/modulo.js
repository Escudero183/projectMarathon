export default {
  getModuloTpl: state => {
    let table = 'Modulo'
    switch (state.operacion) {
      case 'edit':
      case 'new':
        table = `${table}Form`
        break
      default:
        table = `${table}List`
        break
    }
    return table
  },
}
